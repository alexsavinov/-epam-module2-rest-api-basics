package com.epam.esm.service;

import com.epam.esm.dao.giftCertificate.GiftCertificateDao;
import com.epam.esm.exception.GiftCertificateAlreadyExistsException;
import com.epam.esm.exception.GiftCertificateNotFoundException;
import com.epam.esm.model.dto.CreateGiftCertificateRequest;
import com.epam.esm.model.dto.UpdateGiftCertificateRequest;
import com.epam.esm.model.entity.GiftCertificate;
import com.epam.esm.service.mapper.GiftCertificateMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Component
@RequiredArgsConstructor
public class GiftCertificateServiceImpl implements GiftCertificateService {

    private final GiftCertificateMapper certificateMapper;
    private final GiftCertificateDao certificateDao;

    @Override
    public GiftCertificate findById(Long id) {
        log.debug("Looking for a gift certificate with id {}", id);
        GiftCertificate certificate = certificateDao.findById(id)
                .orElseThrow(() -> new GiftCertificateNotFoundException(
                        "Requested resource not found (id = %s)".formatted(id)
                ));

        log.info("Received a gift certificate with id {}", id);
        return certificate;
    }

    @Override
    public List<GiftCertificate> findAll() {
        log.debug("Receiving all gift certificates");
        List<GiftCertificate> certificates = certificateDao.findAll();

        log.info("Received {} gift certificates", certificates.size());
        return certificates;
    }

    @Override
    public GiftCertificate create(CreateGiftCertificateRequest createRequest) {
        log.debug("Creating a new gift certificate");
        GiftCertificate certificate = certificateMapper.toGiftCertificate(createRequest);
        GiftCertificate createdCertificate = certificateDao.save(certificate)
                .orElseThrow(() -> new GiftCertificateAlreadyExistsException(
                        "Requested resource already exists (name = %s)".formatted(createRequest.getName())
                ));

        log.info("Created a new gift certificate with id {}", createdCertificate.getId());
        return createdCertificate;
    }

    @Override
    @Transactional
    public GiftCertificate update(UpdateGiftCertificateRequest updateRequest) {
        log.debug("Updating a gift certificate with id {}", updateRequest.getId());
        GiftCertificate certificate = findById(updateRequest.getId());

        certificate.setName(updateRequest.getName());

        GiftCertificate updatedCertificate = certificateDao.update(certificate);

        log.info("Updated a gift certificate with id {}", updatedCertificate.getId());
        return updatedCertificate;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        log.debug("Deleting gift certificate with id {}", id);
        GiftCertificate certificate = findById(id);

        certificateDao.delete(certificate);
        log.info("Gift certificate with id {} is deleted", certificate.getId());
    }
}
