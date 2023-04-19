package com.epam.esm.model.dto;

import lombok.Getter;

@Getter
public class CreateTagRequest {

    private String name;

    public void setName(String name) {
        this.name = name;
    }
}
