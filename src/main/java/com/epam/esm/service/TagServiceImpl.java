package com.epam.esm.service;

import com.epam.esm.dao.tag.TagDao;
import com.epam.esm.exception.TagAlreadyExistsException;
import com.epam.esm.exception.TagNotFoundException;
import com.epam.esm.model.dto.CreateTagRequest;
import com.epam.esm.model.dto.UpdateTagRequest;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.service.mapper.TagMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@Service
@Component
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagMapper tagMapper;
    private final TagDao tagDao;

    @Override
    public Tag findById(Long id) {
        log.debug("Looking for a tag with id {}", id);
        Tag tag = tagDao.findById(id)
                .orElseThrow(() -> new TagNotFoundException(
                        "Requested resource not found (id = %s)".formatted(id)
                ));

        log.info("Received a tag with id {}", id);
        return tag;
    }

    @Override
    public List<Tag> findAll() {
        log.debug("Receiving all tags");
        List<Tag> tags = tagDao.findAll();

        log.info("Received {} tags", tags.size());
        return tags;
    }

    @Override
    public Tag create(CreateTagRequest createRequest) {
        log.debug("Creating a new tag");
        Tag tag = tagMapper.toTag(createRequest);
        Tag createdTag = tagDao.save(tag)
                .orElseThrow(() -> new TagAlreadyExistsException(
                        "Requested resource already exists (name = %s)".formatted(createRequest.getName())
                ));

        log.info("Created a new tag with id {}", createdTag.getId());
        return createdTag;
    }

    @Override
    @Transactional
    public Tag update(UpdateTagRequest updateRequest) {
        log.debug("Updating a tag with id {}", updateRequest.getId());
        Tag tag = findById(updateRequest.getId());

        tag.setName(updateRequest.getName());

        Tag updatedTag = tagDao.update(tag);

        log.info("Updated a tag with id {}", updatedTag.getId());
        return updatedTag;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        log.debug("Deleting tag with id {}", id);
        Tag tag = findById(id);

        tagDao.delete(tag);
        log.info("Tag with id {} is deleted", tag.getId());
    }

    @Override
    public Tag findByName(String name) {
        log.debug("Looking for a tag with name {}", name);
        Tag tag = tagDao.findByName(name)
                .orElseThrow(() -> new TagNotFoundException(
                        "Requested resource not found (name = %s)".formatted(name)
                ));

        log.info("Found a tag with name {}", name);
        return tag;
    }

    @Override
    public Tag createTagWithCheck(CreateTagRequest createTagRequest) {
        String tagName = createTagRequest.getName();
        log.debug("Looking for a tag with name {}", tagName);

        Tag tag;
        try {
            tag = tagDao.findByName(tagName).get();
            log.debug("Found a tag with name {}", tagName);
        } catch (NoSuchElementException ex) {
            Tag tagNew = tagMapper.toTag(createTagRequest);
            tag = tagDao.save(tagNew).get();
            log.info("Created a new tag with name {}", tagName);
        }

        return tag;
    }
}
