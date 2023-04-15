package com.epam.esm.model.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class GiftCertificateWithTagsDto {

    private Long id;
    private String name;
    private String description;
    private Double price;
    private Integer duration;
    private String createDate;
    private String lastUpdateDate;
    private List<TagDto> tags;
}
