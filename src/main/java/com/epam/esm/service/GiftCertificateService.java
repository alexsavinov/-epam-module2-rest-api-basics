package com.epam.esm.service;

import com.epam.esm.model.dto.CreateGiftCertificateRequest;
import com.epam.esm.model.dto.UpdateGiftCertificateRequest;
import com.epam.esm.model.entity.GiftCertificate;

import java.util.List;

public interface GiftCertificateService {

    GiftCertificate findById(Long id);

    List<GiftCertificate> findAll();

    GiftCertificate create(CreateGiftCertificateRequest createRequest);

    GiftCertificate update(UpdateGiftCertificateRequest updateRequest);

    void delete(Long id);
}
