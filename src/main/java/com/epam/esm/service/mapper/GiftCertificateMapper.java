package com.epam.esm.service.mapper;

import com.epam.esm.model.dto.CreateGiftCertificateRequest;
import com.epam.esm.model.dto.GiftCertificateDto;
import com.epam.esm.model.dto.GiftCertificateWithTagsDto;
import com.epam.esm.model.entity.GiftCertificate;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.util.DateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GiftCertificateMapper {

    private final TagMapper tagMapper;

    public GiftCertificate toGiftCertificate(CreateGiftCertificateRequest createRequest) {
        return GiftCertificate.builder()
                .name(createRequest.getName())
                .description(createRequest.getDescription())
                .duration(createRequest.getDuration())
                .price(createRequest.getPrice())
                .build();
    }

    public GiftCertificateDto toDto(GiftCertificate certificate) {
        return GiftCertificateDto.builder()
                .id(certificate.getId())
                .name(certificate.getName())
                .description(certificate.getDescription())
                .duration(certificate.getDuration())
                .price(certificate.getPrice())
                .createDate(DateUtil.toIso8601Format(certificate.getCreateDate()))
                .lastUpdateDate(DateUtil.toIso8601Format(certificate.getLastUpdateDate()))
                .build();
    }

    public GiftCertificateWithTagsDto toDtoWithTags(GiftCertificate certificate, List<Tag> tags) {
        return GiftCertificateWithTagsDto.builder()
                .id(certificate.getId())
                .name(certificate.getName())
                .description(certificate.getDescription())
                .duration(certificate.getDuration())
                .price(certificate.getPrice())
                .createDate(DateUtil.toIso8601Format(certificate.getCreateDate()))
                .lastUpdateDate(DateUtil.toIso8601Format(certificate.getLastUpdateDate()))
                .tags(tags.stream().map(tagMapper::toDto).toList())
                .build();
    }
}
