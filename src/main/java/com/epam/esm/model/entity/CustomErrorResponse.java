package com.epam.esm.model.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomErrorResponse {

    private String errorMessage;
    private int errorCode;
}