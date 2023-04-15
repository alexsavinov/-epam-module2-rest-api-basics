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

//    @PatchMapping(value = "/{id}", produces = MediaType.TEXT_PLAIN_VALUE)
//    public String updateCertificate(@RequestParam Map<String, String> parameters, @PathVariable Long id) {
//
//        String price = parameters.get("price");
//        String duration = parameters.get("duration");
//        String tags = parameters.get("tags");
//
//        GiftCertificate certificate = GiftCertificate.builder()
//                .id(id)
//                .name(parameters.get("name"))
//                .description(parameters.get("description"))
//                .build();
//
//        if (price != null) {
//            certificate.setPrice(Double.parseDouble(price));
//        }
//        if (duration != null) {
//            certificate.setDuration(Integer.parseInt(duration));
//        }
//
//        String[] tagsArray = tags.split(",");
//        List<Tag> tagsList = new ArrayList<>();
//        for (String tagName: tagsArray) {
//            Tag tag = Tag.builder().name(tagName).build();
//            Tag tagNew = tagDao.save(tag).get();
//            tagsList.add(tagNew);
//        }
//
//        GiftCertificate certificateNew = giftCertificateDao.update(certificate);
//
//        return certificateNew + " -- " + tagsList;
//    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCertificateById(@PathVariable Long id) {
        certificateService.delete(id);
    }
}