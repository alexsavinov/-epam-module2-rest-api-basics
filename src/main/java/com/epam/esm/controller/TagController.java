package com.epam.esm.controller;

import com.epam.esm.model.dto.CreateTagRequest;
import com.epam.esm.model.dto.TagDto;
import com.epam.esm.model.dto.UpdateTagRequest;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.service.TagService;
import com.epam.esm.service.mapper.TagMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping({"/tags"})
public class TagController {

    private final TagMapper tagMapper;
    private final TagService tagService;

    @GetMapping(value = "/{id}")
    public TagDto getTagById(@PathVariable Long id) {
        Tag tag = tagService.findById(id);
        TagDto tagDto = tagMapper.toDto(tag);

        return tagDto;
    }

    @GetMapping
    public List<TagDto> getAllTags() {
        List<Tag> tags = tagService.findAll();
        List<TagDto> tagsDto = tags
                .stream()
                .map(tagMapper::toDto)
                .collect(Collectors.toList());

        return tagsDto;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TagDto addTag(@RequestBody CreateTagRequest createTagRequest) {
        Tag tag = tagService.create(createTagRequest);
        TagDto tagDto = tagMapper.toDto(tag);

        return tagDto;
    }

    @PatchMapping
    public TagDto updateTag(@RequestBody UpdateTagRequest updateTagRequest) {
        Tag updatedTag = tagService.update(updateTagRequest);
        TagDto tagDto = tagMapper.toDto(updatedTag);

        return tagDto;
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTagById(@PathVariable Long id) {
        tagService.delete(id);
    }

}