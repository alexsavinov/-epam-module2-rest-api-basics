package com.epam.esm.model.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class TagDto  {

    private Long id;
    private String name;
}
