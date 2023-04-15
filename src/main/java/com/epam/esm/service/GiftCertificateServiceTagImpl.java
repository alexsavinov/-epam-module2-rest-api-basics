package com.epam.esm.service;

import com.epam.esm.dao.giftCertificateTag.GiftCertificateTagDao;
import com.epam.esm.model.dto.CreateTagRequest;
import com.epam.esm.model.dto.SearchRequest;
import com.epam.esm.model.entity.GiftCertificate;
import com.epam.esm.model.entity.Tag;
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
public class GiftCertificateServiceTagImpl implements GiftCertificateTagService {

    private final GiftCertificateTagDao certificateTagDao;
    private final TagService tagService;

    @Override
    public List<Tag> findTagsByCertificateId(Long id) {
        log.debug("Looking for a tags by certificate id {}", id);
        List<Tag> tags = certificateTagDao.findTagsByCertificateId(id);

        log.info("Received {} tags of certificate id {}", tags.size(), id);
        return tags;
    }

    @Override
    public List<GiftCertificate> findCertificateWithSearchParams(SearchRequest searchRequest) {
        log.debug("Looking for a certificates by search params");
        List<GiftCertificate> certificates = certificateTagDao
                .findCertificateWithSearchParams(searchRequest);

        log.info("Received {} certificates by search params", certificates.size());
        return certificates;
    }

    @Override
    @Transactional
    public List<Tag> updateTagsByCertificateId(Long tagId, List<CreateTagRequest> createTagRequests) {
        log.debug("Updating certificate id {} with {} tags", tagId, createTagRequests.size());
        certificateTagDao.deleteLinkOfCertificateAndTags(tagId);

        List<Tag> tags = createTagRequests
                .stream()
                .map(tagService::createTagWithCheck)
                .toList();

        tags.forEach(tag -> certificateTagDao
                .updateCertificateWithTag(tagId, tag.getId()));

        log.info("Updated certificate id {} with {} tags", tagId, tags.size());
        return tags;
    }
}
