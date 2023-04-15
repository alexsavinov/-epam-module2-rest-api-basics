package com.epam.esm.model.dto;

import lombok.Getter;

@Getter
public class UpdateGiftCertificateRequest {

    private Long id;
    private String name;
    private String description;
    private Double price;
    private int duration;
}
