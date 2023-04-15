package com.epam.esm.service;

import com.epam.esm.model.dto.CreateTagRequest;
import com.epam.esm.model.dto.UpdateTagRequest;
import com.epam.esm.model.entity.Tag;

import java.util.List;

public interface TagService {

    Tag findById(Long id);

    List<Tag> findAll();

    Tag create(CreateTagRequest createRequest);
    Tag createTagWithCheck(CreateTagRequest createTagRequest);

    Tag update(UpdateTagRequest updateRequest);

    void delete(Long id);

    Tag findByName(String name);

}
