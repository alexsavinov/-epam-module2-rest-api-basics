package com.epam.esm.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GiftCertificate {
    private Long id;
    private String name;
    private String description;
    private Double price;
    private Integer duration;
    private Timestamp createDate;
    private Timestamp lastUpdateDate;
}
