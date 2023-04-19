package com.epam.esm.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchRequest {

    private String name;
    private String description;
    private String tag;
    private String sortByName;
    private String sortByCreateDate;
    private String sortByUpdateDate;
}
