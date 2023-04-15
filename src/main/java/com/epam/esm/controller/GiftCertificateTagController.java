package com.epam.esm.controller;

import com.epam.esm.model.dto.CreateTagRequest;
import com.epam.esm.model.dto.GiftCertificateDto;
import com.epam.esm.model.dto.SearchRequest;
import com.epam.esm.model.dto.TagDto;
import com.epam.esm.model.entity.GiftCertificate;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.service.GiftCertificateTagService;
import com.epam.esm.service.mapper.GiftCertificateMapper;
import com.epam.esm.service.mapper.TagMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class GiftCertificateTagController {

    private final GiftCertificateTagService certificateTagService;
    private final GiftCertificateMapper certificateMapper;
    private final TagMapper tagMapper;

    @PatchMapping("/{id}")
    public List<TagDto> updateTagsForCertificate(
            @PathVariable Long id,
            @RequestBody List<CreateTagRequest> createTagRequests) {

        List<Tag> tags = certificateTagService
                .updateTagsByCertificateId(id, createTagRequests);

        List<TagDto> tagsDto = tags
                .stream()
                .map(tagMapper::toDto)
                .collect(Collectors.toList());

        return tagsDto;
    }

    @PostMapping
    public List<GiftCertificateDto> searchCertificateWithSearchParams(
            @RequestBody SearchRequest searchRequest) {

        List<GiftCertificate> certificates = certificateTagService
                .findCertificateWithSearchParams(searchRequest);

        List<GiftCertificateDto> certificatesDto = certificates
                .stream()
                .map(certificateMapper::toDto)
                .collect(Collectors.toList());

        return certificatesDto;
    }
}
