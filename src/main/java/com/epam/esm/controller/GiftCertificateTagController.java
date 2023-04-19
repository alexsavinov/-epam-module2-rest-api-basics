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

    @GetMapping
    public List<GiftCertificateDto> searchCertificatesWithSearchParams(
            @RequestParam(required = false) String tag,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) List<String> sortBy,
            @RequestParam(required = false) List<String> sortDirection) {

        SearchRequest searchRequest = new SearchRequest();
        searchRequest.setTag(tag);
        searchRequest.setName(name);
        searchRequest.setDescription(description);

        for (int i = 0; i < sortBy.size(); i++) {
            String sortDirectionValue = sortDirection.get(i);
            if (sortBy.get(i).equals("name")) {
                searchRequest.setSortByName(sortDirectionValue);
            } else if (sortBy.get(i).equals("createDate")) {
                searchRequest.setSortByUpdateDate(sortDirectionValue);
            } else if (sortBy.get(i).equals("updateDate")) {
                searchRequest.setSortByCreateDate(sortDirectionValue);
            }
        }

        List<GiftCertificate> certificates = certificateTagService
                .findCertificateWithSearchParams(searchRequest);

        List<GiftCertificateDto> certificatesDto = certificates
                .stream()
                .map(certificateMapper::toDto)
                .collect(Collectors.toList());

        return certificatesDto;
    }
}
