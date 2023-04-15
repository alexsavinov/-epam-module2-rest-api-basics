package com.epam.esm.service;

import com.epam.esm.model.dto.CreateTagRequest;
import com.epam.esm.model.dto.SearchRequest;
import com.epam.esm.model.entity.GiftCertificate;
import com.epam.esm.model.entity.Tag;

import java.util.List;

public interface GiftCertificateTagService {

    List<Tag> findTagsByCertificateId(Long id);

    List<GiftCertificate> findCertificateWithSearchParams(SearchRequest searchRequest);

    List<Tag> updateTagsByCertificateId(Long id, List<CreateTagRequest> createRequests);
}
