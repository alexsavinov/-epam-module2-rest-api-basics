package com.epam.esm.controller;

import com.epam.esm.exception.GiftCertificateNotFoundException;
import com.epam.esm.model.dto.*;
import com.epam.esm.model.entity.GiftCertificate;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.service.GiftCertificateService;
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
class GiftCertificateControllerTest {
    private static final Long CERTIFICATE_ID = 1L;
    @Mock
    private GiftCertificateService certificateService;
    @Mock
    private GiftCertificateTagService certificateTagService;
    @Mock
    private GiftCertificateMapper certificateMapper;
    @InjectMocks
    private GiftCertificateController subject;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(subject)
                .setControllerAdvice(new CustomGlobalExceptionHandler())
                .build();
    }

    @Test
    void getGiftCertificateById() throws Exception {
        GiftCertificate certificate = new GiftCertificate();

        Tag expectedTag = Tag.builder().name("tag1").build();
        TagMapper tagMapper = new TagMapper();
        tagMapper.toDto(expectedTag);

        List<Tag> tags = new ArrayList<>();
        tags.add(expectedTag);

        List<TagDto> expectedTags = new ArrayList<>();
        expectedTags.add(tagMapper.toDto(expectedTag));

        GiftCertificateWithTagsDto certificateWithTagsDto = GiftCertificateWithTagsDto.builder()
                .tags(expectedTags)
                .id(CERTIFICATE_ID)
                .name("myGiftCertificate")
                .build();

        when(certificateService.findById(any(Long.class))).thenReturn(certificate);
        when(certificateTagService.findTagsByCertificateId(any(Long.class))).thenReturn(tags);
        when(certificateMapper.toDtoWithTags(any(GiftCertificate.class), anyList()))
            .thenReturn(certificateWithTagsDto);

        mockMvc.perform(
                        get("/certificates/{id}", CERTIFICATE_ID)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(certificateWithTagsDto.getId().toString()))
                .andExpect(jsonPath("$.name").value(certificateWithTagsDto.getName()))
                .andExpect(jsonPath("$.description").value(certificateWithTagsDto.getDescription()))
                .andExpect(jsonPath("$.price").value(certificateWithTagsDto.getPrice()))
                .andExpect(jsonPath("$.duration").value(certificateWithTagsDto.getDuration()));

        verify(certificateService).findById(CERTIFICATE_ID);
        verify(certificateMapper).toDtoWithTags(certificate, tags);
        verifyNoMoreInteractions(certificateService, certificateMapper);
    }

    @Test
    void getGiftCertificateById_whenGiftCertificateNotFoundExceptionIsThrows_returns404() throws Exception {
        when(certificateService.findById(any(Long.class)))
                .thenThrow(new GiftCertificateNotFoundException("GiftCertificate not found"));

        mockMvc.perform(
                        get("/certificates/{id}", CERTIFICATE_ID)
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorMessage").value("GiftCertificate not found"));

        verify(certificateService).findById(CERTIFICATE_ID);
        verifyNoInteractions(certificateMapper);
        verifyNoMoreInteractions(certificateService);
    }

    @Test
    void getAllGiftCertificates() throws Exception {
        GiftCertificate certificate = new GiftCertificate();
        GiftCertificateDto certificateDto = GiftCertificateDto.builder()
                .id(CERTIFICATE_ID)
                .name("myGiftCertificate")
                .build();

        List<GiftCertificate> certificates = new ArrayList<>();
        certificates.add(certificate);

        when(certificateService.findAll()).thenReturn(certificates);
        when(certificateMapper.toDto(any(GiftCertificate.class))).thenReturn(certificateDto);

        mockMvc.perform(
                        get("/certificates")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[*].id", containsInAnyOrder(certificateDto.getId().intValue())))
                .andExpect(jsonPath("$[*].name", containsInAnyOrder(certificateDto.getName())))
                .andExpect(jsonPath("$[*].description", containsInAnyOrder(certificateDto.getDescription())))
                .andExpect(jsonPath("$[*].price", containsInAnyOrder(certificateDto.getPrice())))
                .andExpect(jsonPath("$[*].duration", containsInAnyOrder(certificateDto.getDuration())));

        verify(certificateService).findAll();
        verify(certificateMapper).toDto(certificate);
        verifyNoMoreInteractions(certificateService, certificateMapper);
    }

    @Test
    void addGiftCertificate() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();

        GiftCertificate certificate = new GiftCertificate();
        GiftCertificateDto certificateDto = GiftCertificateDto.builder()
                .id(CERTIFICATE_ID)
                .name("myGiftCertificate")
                .build();

        when(certificateService.create(any(CreateGiftCertificateRequest.class))).thenReturn(certificate);
        when(certificateMapper.toDto(any(GiftCertificate.class))).thenReturn(certificateDto);

        RequestBuilder requestBuilder = post("/certificates")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsString(certificate))
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(certificateDto.getId().toString()))
                .andExpect(jsonPath("$.name").value(certificateDto.getName()))
                .andExpect(jsonPath("$.description").value(certificateDto.getDescription()))
                .andExpect(jsonPath("$.price").value(certificateDto.getPrice()))
                .andExpect(jsonPath("$.duration").value(certificateDto.getDuration()));

        verify(certificateMapper).toDto(certificate);
        verifyNoMoreInteractions(certificateService, certificateMapper);
    }

    @Test
    void updateGiftCertificate() throws Exception {
        GiftCertificate certificate = new GiftCertificate();
        GiftCertificateDto certificateDto = GiftCertificateDto.builder()
                .id(CERTIFICATE_ID)
                .name("myGiftCertificate")
                .build();

        when(certificateService.update(any(UpdateGiftCertificateRequest.class))).thenReturn(certificate);
        when(certificateMapper.toDto(any(GiftCertificate.class))).thenReturn(certificateDto);

        ObjectMapper objectMapper = new ObjectMapper();

        RequestBuilder requestBuilder = patch("/certificates")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsString(certificate))
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(certificateDto.getId().toString()))
                .andExpect(jsonPath("$.name").value(certificateDto.getName()))
                .andExpect(jsonPath("$.description").value(certificateDto.getDescription()))
                .andExpect(jsonPath("$.price").value(certificateDto.getPrice()))
                .andExpect(jsonPath("$.duration").value(certificateDto.getDuration()));

        verify(certificateMapper).toDto(certificate);
        verifyNoMoreInteractions(certificateService, certificateMapper);
    }

    @Test
    void deleteGiftCertificateById() throws Exception {
        RequestBuilder requestBuilder = delete("/certificates/{id}", CERTIFICATE_ID);

        mockMvc.perform(requestBuilder).andExpect(status().isNoContent());

        verify(certificateService).delete(CERTIFICATE_ID);
        verifyNoMoreInteractions(certificateService);
    }
}