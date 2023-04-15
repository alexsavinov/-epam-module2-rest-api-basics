package com.epam.esm.controller;

import com.epam.esm.model.dto.CreateGiftCertificateRequest;
import com.epam.esm.model.dto.GiftCertificateDto;
import com.epam.esm.model.dto.GiftCertificateWithTagsDto;
import com.epam.esm.model.entity.GiftCertificate;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.GiftCertificateTagService;
import com.epam.esm.service.mapper.GiftCertificateMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping({"/certificates"})
public class GiftCertificateController {

    private final GiftCertificateMapper certificateMapper;
    private final GiftCertificateService certificateService;
    private final GiftCertificateTagService certificateTagService;

    @GetMapping(value = "/{id}")
    public GiftCertificateWithTagsDto getCertificateById(@PathVariable Long id) {
        GiftCertificate certificate = certificateService.findById(id);

        List<Tag> tags = certificateTagService.findTagsByCertificateId(id);

        /* Display Tags only for single Gift certificate */
        GiftCertificateWithTagsDto certificateTagsDto = certificateMapper
                .toDtoWithTags(certificate, tags);

        return certificateTagsDto;
    }

    @GetMapping
    public List<GiftCertificateDto> getAllCertificates() {
        List<GiftCertificate> certificates = certificateService.findAll();

        List<GiftCertificateDto> certificatesDto = certificates
                .stream()
                .map(certificateMapper::toDto)
                .collect(Collectors.toList());

        return certificatesDto;
    }

    @PutMapping
    public GiftCertificateDto addCertificate(@RequestBody CreateGiftCertificateRequest createCertificateRequest) {
        GiftCertificate certificate = certificateService.create(createCertificateRequest);
        GiftCertificateDto certificateDto = certificateMapper.toDto(certificate);

        return certificateDto;
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCertificateById(@PathVariable Long id) {
        certificateService.delete(id);
    }
}