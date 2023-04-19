package com.epam.esm.controller;

import com.epam.esm.model.dto.*;
import com.epam.esm.model.entity.GiftCertificate;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.service.GiftCertificateTagService;
import com.epam.esm.service.mapper.GiftCertificateMapper;
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
class GiftCertificateTagControllerTest {
    private static final Long CERTIFICATE_ID = 1L;
    @Mock
    private GiftCertificateTagService certificateTagService;
    @Mock
    private TagMapper tagMapper;
    @Mock
    private GiftCertificateMapper certificateMapper;
    @InjectMocks
    private GiftCertificateTagController subject;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(subject)
                .setControllerAdvice(new CustomGlobalExceptionHandler())
                .build();
    }

    @Test
    void updateTagsForCertificate() throws Exception {
        Tag expectedTag = Tag.builder().id(1L).name("tag1").build();
        List<Tag> tags = new ArrayList<>();
        tags.add(expectedTag);

        TagDto tagDto = new TagMapper().toDto(expectedTag);
        List<TagDto> expectedTags = new ArrayList<>();
        expectedTags.add(tagDto);

        CreateTagRequest createTagRequest = new CreateTagRequest();
        createTagRequest.setName("tag1");
        List<CreateTagRequest> createTagRequests = new ArrayList<>();
        createTagRequests.add(createTagRequest);

        when(certificateTagService.updateTagsByCertificateId(any(Long.class), anyList()))
                .thenReturn(tags);
        when(tagMapper.toDto(any(Tag.class))).thenReturn(tagDto);

        ObjectMapper objectMapper = new ObjectMapper();

        RequestBuilder requestBuilder = patch("/{id}", CERTIFICATE_ID)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsString(createTagRequests))
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[*].id", containsInAnyOrder(expectedTag.getId().intValue())))
                .andExpect(jsonPath("$[*].name", containsInAnyOrder(expectedTag.getName())));

        verify(tagMapper).toDto(expectedTag);
        verifyNoMoreInteractions(tagMapper);
    }

    @Test
    void searchCertificatesWithSearchParams() throws Exception {
        GiftCertificate certificate = new GiftCertificate();
        GiftCertificateDto certificateDto = GiftCertificateDto.builder()
                .id(CERTIFICATE_ID)
                .name("name1")
                .build();

        List<GiftCertificate> certificates = new ArrayList<>();
        certificates.add(certificate);

        when(certificateTagService.findCertificateWithSearchParams(any(SearchRequest.class))).thenReturn(certificates);
        when(certificateMapper.toDto(any(GiftCertificate.class))).thenReturn(certificateDto);

        mockMvc.perform(
                        get("/")
                                .param("name", certificateDto.getName())
                                .param("description", certificateDto.getDescription())
                                .param("tag", "tag1")
                                .param("sortBy", "name")
                                .param("sortDirection", "ASC")
                                .param("sortBy", "updateDate")
                                .param("sortDirection", "DESC")
                                .param("sortBy", "createDate")
                                .param("sortDirection", "DESC")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[*].id", containsInAnyOrder(certificateDto.getId().intValue())))
                .andExpect(jsonPath("$[*].name", containsInAnyOrder(certificateDto.getName())))
                .andExpect(jsonPath("$[*].description", containsInAnyOrder(certificateDto.getDescription())))
                .andExpect(jsonPath("$[*].price", containsInAnyOrder(certificateDto.getPrice())))
                .andExpect(jsonPath("$[*].duration", containsInAnyOrder(certificateDto.getDuration())));

        verify(certificateMapper).toDto(certificate);
        verifyNoMoreInteractions( certificateMapper);
    }
}