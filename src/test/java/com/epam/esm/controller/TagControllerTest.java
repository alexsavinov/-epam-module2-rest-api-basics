package com.epam.esm.controller;

import com.epam.esm.exception.TagNotFoundException;
import com.epam.esm.model.dto.CreateTagRequest;
import com.epam.esm.model.dto.TagDto;
import com.epam.esm.model.dto.UpdateTagRequest;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.service.TagService;
import com.epam.esm.service.mapper.TagMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class TagControllerTest {
    private static final Long TAG_ID = 1L;
    @Mock
    private TagService tagService;
    @Mock
    private TagMapper tagMapper;
    @InjectMocks
    private TagController subject;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(subject)
                .setControllerAdvice(new CustomGlobalExceptionHandler())
                .build();
    }

    @Test
    void getTagById() throws Exception {
        Tag tag = new Tag();
        TagDto tagDto = TagDto.builder()
                .id(TAG_ID)
                .name("myTag")
                .build();

        when(tagService.findById(any(Long.class))).thenReturn(tag);
        when(tagMapper.toDto(any(Tag.class))).thenReturn(tagDto);

        mockMvc.perform(
                        get("/tags/{id}", TAG_ID)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(tagDto.getId().toString()))
                .andExpect(jsonPath("$.name").value(tagDto.getName()));

        verify(tagService).findById(TAG_ID);
        verify(tagMapper).toDto(tag);
        verifyNoMoreInteractions(tagService, tagMapper);
    }

    @Test
    void getTagById_whenTagNotFoundExceptionIsThrows_returns404() throws Exception {
        when(tagService.findById(any(Long.class))).thenThrow(new TagNotFoundException("Tag not found"));

        mockMvc.perform(
                        get("/tags/{id}", TAG_ID)
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorMessage").value("Tag not found"));

        verify(tagService).findById(TAG_ID);
        verifyNoInteractions(tagMapper);
        verifyNoMoreInteractions(tagService);
    }

    @Test
    void getAllTags() throws Exception {
        Tag tag = new Tag();
        TagDto tagDto = TagDto.builder()
                .id(TAG_ID)
                .name("myTag")
                .build();

        List<Tag> tags = new ArrayList<>();
        tags.add(tag);

        when(tagService.findAll()).thenReturn(tags);
        when(tagMapper.toDto(any(Tag.class))).thenReturn(tagDto);

        mockMvc.perform(
                        get("/tags")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[*].id", containsInAnyOrder(tagDto.getId().intValue())))
                .andExpect(jsonPath("$[*].name", containsInAnyOrder(tagDto.getName()))
                );

        verify(tagService).findAll();
        verify(tagMapper).toDto(tag);
        verifyNoMoreInteractions(tagService, tagMapper);
    }

    @Test
    void addTag() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();

        Tag tag = new Tag();
        TagDto tagDto = TagDto.builder()
                .id(TAG_ID)
                .name("myTag")
                .build();

        when(tagService.create(any(CreateTagRequest.class))).thenReturn(tag);
        when(tagMapper.toDto(any(Tag.class))).thenReturn(tagDto);

        RequestBuilder requestBuilder = put("/tags")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsString(tag))
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(tagDto.getId().toString()))
                .andExpect(jsonPath("$.name").value(tagDto.getName()));
//                .andDo(print());

//        verify(tagService).create(createTagRequest);
        verify(tagMapper).toDto(tag);
        verifyNoMoreInteractions(tagService, tagMapper);
    }

    @Test
    void updateTag() throws Exception {
        Tag tag = new Tag();
        TagDto tagDto = TagDto.builder()
                .id(TAG_ID)
                .name("myTag")
                .build();

        when(tagService.update(any(UpdateTagRequest.class))).thenReturn(tag);
        when(tagMapper.toDto(any(Tag.class))).thenReturn(tagDto);

        ObjectMapper objectMapper = new ObjectMapper();

        RequestBuilder requestBuilder = patch("/tags")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsString(tag))
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(tagDto.getId().toString()))
                .andExpect(jsonPath("$.name").value(tagDto.getName()));

        verify(tagMapper).toDto(tag);
        verifyNoMoreInteractions(tagService, tagMapper);
    }

    @Test
    void deleteTagById() throws Exception {
        RequestBuilder requestBuilder = delete("/tags/{id}", TAG_ID);

        mockMvc.perform(requestBuilder).andExpect(status().isNoContent());

        verify(tagService).delete(TAG_ID);
        verifyNoMoreInteractions(tagService);
    }
}