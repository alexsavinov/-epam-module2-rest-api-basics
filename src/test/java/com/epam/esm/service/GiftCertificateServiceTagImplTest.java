package com.epam.esm.service;

import com.epam.esm.dao.giftCertificateTag.GiftCertificateTagDao;
import com.epam.esm.model.dto.CreateTagRequest;
import com.epam.esm.model.dto.SearchRequest;
import com.epam.esm.model.entity.GiftCertificate;
import com.epam.esm.model.entity.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GiftCertificateServiceTagImplTest {
    private static final Long CERTIFICATE_ID = 1L;
    @Mock
    private GiftCertificateTagDao certificateTagDao;
    @Mock
    private SearchRequest searchRequest;
    @Mock
    TagServiceImpl tagService;

    @InjectMocks
    private GiftCertificateServiceTagImpl subject;

    @Test
    void findTagsByCertificateId() {
        Tag expectedTag1 = new Tag(1L, "tag1");
        Tag expectedTag2 = new Tag(2L, "tag2");

        List<Tag> expectedTags = new ArrayList<>();
        expectedTags.add(expectedTag1);
        expectedTags.add(expectedTag2);

        when(certificateTagDao.findTagsByCertificateId(any(Long.class))).thenReturn(expectedTags);

        List<Tag> actualTags = subject.findTagsByCertificateId(CERTIFICATE_ID);

        verify(certificateTagDao).findTagsByCertificateId(CERTIFICATE_ID);
        verifyNoMoreInteractions(certificateTagDao);

        assertThat(actualTags).isEqualTo(expectedTags);
    }

    @Test
    void findCertificateWithSearchParams() {
        GiftCertificate expectedCertificate1 = GiftCertificate.builder()
                .id(1L)
                .name("cert1")
                .description("desc1")
                .duration(5)
                .price(2.2)
                .createDate(java.sql.Timestamp.valueOf("2023-04-11 11:00:00.12221"))
                .lastUpdateDate(java.sql.Timestamp.valueOf("2023-04-14 20:00:00.11111"))
                .build();
        GiftCertificate expectedCertificate2 = GiftCertificate.builder()
                .id(1L)
                .name("cert2")
                .description("desc2")
                .duration(6)
                .price(1.1)
                .createDate(java.sql.Timestamp.valueOf("2023-04-15 10:00:00.22222"))
                .lastUpdateDate(java.sql.Timestamp.valueOf("2023-04-14 18:00:00.11111"))
                .build();

        List<GiftCertificate> expectedCertificates = new ArrayList<>();
        expectedCertificates.add(expectedCertificate1);
        expectedCertificates.add(expectedCertificate2);

        when(certificateTagDao.findCertificateWithSearchParams(any(SearchRequest.class)))
                .thenReturn(expectedCertificates);

        List<GiftCertificate> actualCertificates = subject
                .findCertificateWithSearchParams(searchRequest);

        verify(certificateTagDao).findCertificateWithSearchParams(searchRequest);
        verifyNoMoreInteractions(certificateTagDao);

        assertThat(actualCertificates).isEqualTo(expectedCertificates);
    }

    @Test
    void updateTagsByCertificateId() {
        CreateTagRequest createTagRequest = new CreateTagRequest();
        List<CreateTagRequest> createTagRequests = new ArrayList<>();
        createTagRequests.add(createTagRequest);

        Tag expectedTag = new Tag(1L, "tag1");
        List<Tag> expectedTags = new ArrayList<>();
        expectedTags.add(expectedTag);

        when(tagService.createTagWithCheck(any(CreateTagRequest.class)))
                .thenReturn(expectedTag);

        List<Tag> actualTags = subject.updateTagsByCertificateId(CERTIFICATE_ID, createTagRequests);

        verify(certificateTagDao).deleteLinkOfCertificateAndTags(any(Long.class));
        verify(certificateTagDao).updateCertificateWithTag(any(Long.class), any(Long.class));
        verifyNoMoreInteractions(certificateTagDao);

        assertThat(expectedTags).isEqualTo(actualTags);
    }
}