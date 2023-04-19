package com.epam.esm.service;

import com.epam.esm.dao.giftCertificate.GiftCertificateDao;
import com.epam.esm.exception.GiftCertificateAlreadyExistsException;
import com.epam.esm.exception.GiftCertificateNotFoundException;
import com.epam.esm.model.dto.CreateGiftCertificateRequest;
import com.epam.esm.model.dto.UpdateGiftCertificateRequest;
import com.epam.esm.model.entity.GiftCertificate;
import com.epam.esm.service.mapper.GiftCertificateMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GiftCertificateServiceImplTest {
    private static final Long CERTIFICATE_ID = 1L;
    @Mock
    private GiftCertificateDao giftCertificateDao;
    @Mock
    private GiftCertificateMapper giftCertificateMapper;
    @Mock
    private CreateGiftCertificateRequest createRequest;
    @Mock
    private UpdateGiftCertificateRequest updateRequest;

    @InjectMocks
    private GiftCertificateServiceImpl subject;

    @Test
    void findById() {
        GiftCertificate expectedGiftCertificate = new GiftCertificate();

        when(giftCertificateDao.findById(any(Long.class))).thenReturn(Optional.of(expectedGiftCertificate));

        GiftCertificate actualGiftCertificate = subject.findById(CERTIFICATE_ID);

        verify(giftCertificateDao).findById(CERTIFICATE_ID);
        verifyNoMoreInteractions(giftCertificateDao);

        assertThat(actualGiftCertificate).isEqualTo(expectedGiftCertificate);
    }

    @Test
    void findById_whenGiftCertificateIsNotFoundById_throwsGiftCertificateNotFoundException() {
        when(giftCertificateDao.findById(any(Long.class))).thenReturn(Optional.empty());

        GiftCertificateNotFoundException exception = assertThrows(GiftCertificateNotFoundException.class,
                () -> subject.findById(CERTIFICATE_ID));

        verify(giftCertificateDao).findById(CERTIFICATE_ID);
        verifyNoMoreInteractions(giftCertificateDao);

        String expectedMessage = "Requested resource not found (id = %s)".formatted(CERTIFICATE_ID);
        assertThat(exception.getMessage()).isEqualTo(expectedMessage);
    }

    @Test
    void findAll() {
        List<GiftCertificate> expectedGiftCertificates = new ArrayList<>();
        expectedGiftCertificates.add(new GiftCertificate());

        when(giftCertificateDao.findAll()).thenReturn(expectedGiftCertificates);

        List<GiftCertificate> actualGiftCertificates = subject.findAll();

        verify(giftCertificateDao).findAll();
        verifyNoMoreInteractions(giftCertificateDao);

        assertThat(actualGiftCertificates).isEqualTo(expectedGiftCertificates);
    }

    @Test
    void create() {
        GiftCertificate newGiftCertificate = new GiftCertificate();
        GiftCertificate expectedGiftCertificate = new GiftCertificate();

        when(giftCertificateMapper.toGiftCertificate(createRequest)).thenReturn(newGiftCertificate);
        when(giftCertificateDao.save(any(GiftCertificate.class))).thenReturn(Optional.of(newGiftCertificate));

        GiftCertificate actualGiftCertificate = subject.create(createRequest);

        verify(giftCertificateDao).save(newGiftCertificate);
        verify(giftCertificateMapper).toGiftCertificate(createRequest);
        verifyNoMoreInteractions(giftCertificateDao);

        assertThat(actualGiftCertificate).isEqualTo(expectedGiftCertificate);
    }

    @Test
    void create_whenGiftCertificateWithNameExists_throwsGiftCertificateAlreadyExistsException() {
        GiftCertificate newGiftCertificate = GiftCertificate.builder()
                .id(CERTIFICATE_ID)
                .name("name1")
                .description("description1")
                .price(11.22)
                .duration(5)
                .build();

        when(giftCertificateMapper.toGiftCertificate(createRequest)).thenReturn(newGiftCertificate);
        when(createRequest.getName()).thenReturn(newGiftCertificate.getName());

        GiftCertificateAlreadyExistsException exception = assertThrows(GiftCertificateAlreadyExistsException.class,
                () -> subject.create(createRequest));

        verify(giftCertificateDao).save(newGiftCertificate);
        verify(giftCertificateMapper).toGiftCertificate(createRequest);
        verifyNoMoreInteractions(giftCertificateDao);

        String expectedMessage = "Requested resource already exists (name = %s)".formatted(newGiftCertificate.getName());
        assertThat(exception.getMessage()).isEqualTo(expectedMessage);
    }

    @Test
    void update() {
        GiftCertificate updateGiftCertificate = GiftCertificate.builder()
                .id(CERTIFICATE_ID)
                .name("name1")
                .description("description1")
                .price(11.22)
                .duration(5)
                .build();
        GiftCertificate expectedGiftCertificate = GiftCertificate.builder()
                .id(CERTIFICATE_ID)
                .name("name1")
                .description("description1")
                .price(11.22)
                .duration(5)
                .build();

        when(updateRequest.getName()).thenReturn("name1");
        when(updateRequest.getDescription()).thenReturn("description1");
        when(updateRequest.getPrice()).thenReturn(11.22);
        when(updateRequest.getDuration()).thenReturn(5);
        when(giftCertificateDao.findById(any(Long.class))).thenReturn(Optional.of(updateGiftCertificate));
        when(giftCertificateDao.update(any(GiftCertificate.class))).thenReturn(updateGiftCertificate);

        GiftCertificate actualGiftCertificate = subject.update(updateRequest);

        verify(giftCertificateDao).update(updateGiftCertificate);
        verifyNoMoreInteractions(giftCertificateDao);

        assertThat(actualGiftCertificate).isEqualTo(expectedGiftCertificate);
    }

    @Test
    void delete() {
        GiftCertificate deleteGiftCertificate = GiftCertificate.builder()
                .id(CERTIFICATE_ID)
                .name("name1")
                .description("description1")
                .price(11.22)
                .duration(5)
                .build();

        when(giftCertificateDao.findById(any(Long.class))).thenReturn(Optional.of(deleteGiftCertificate));

        subject.delete(CERTIFICATE_ID);

        verify(giftCertificateDao).delete(deleteGiftCertificate);
        verifyNoMoreInteractions(giftCertificateDao);
    }
}