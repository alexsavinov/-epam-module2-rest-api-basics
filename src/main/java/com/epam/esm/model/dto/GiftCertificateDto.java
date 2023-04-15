package com.epam.esm.model.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class GiftCertificateDto {

    private Long id;
    private String name;
    private String description;
    private Double price;
    private Integer duration;
    private String createDate;
    private String lastUpdateDate;
}
