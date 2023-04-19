package com.epam.esm.model.dto;

import lombok.Getter;

@Getter
public class CreateGiftCertificateRequest {

    private String name;
    private String description;
    private Double price;
    private Integer duration;
}
