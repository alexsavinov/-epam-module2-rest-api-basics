package com.epam.esm.service;

import com.epam.esm.dao.tag.TagDao;
import com.epam.esm.exception.TagAlreadyExistsException;
import com.epam.esm.exception.TagNotFoundException;
import com.epam.esm.model.dto.CreateTagRequest;
import com.epam.esm.model.dto.UpdateTagRequest;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.service.mapper.TagMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TagServiceImplTest {
    private static final Long TAG_ID = 1L;
    @Mock
    private TagDao tagDao;
    @Mock
    private TagMapper tagMapper;
    @Mock
    private CreateTagRequest createRequest;
    @Mock
    private UpdateTagRequest updateRequest;

    @InjectMocks
    private TagServiceImpl subject;

    @Test
    void findById() {
        Tag expectedTag = new Tag();

        when(tagDao.findById(any(Long.class))).thenReturn(Optional.of(expectedTag));

        Tag actualTag = subject.findById(TAG_ID);

        verify(tagDao).findById(TAG_ID);
        verifyNoMoreInteractions(tagDao);

        assertThat(actualTag).isEqualTo(expectedTag);
    }

    @Test
    void findById_whenTagIsNotFoundById_throwsTagNotFoundException() {
        when(tagDao.findById(any(Long.class))).thenReturn(Optional.empty());

        TagNotFoundException exception = assertThrows(TagNotFoundException.class,
                () -> subject.findById(TAG_ID));

        verify(tagDao).findById(TAG_ID);
        verifyNoMoreInteractions(tagDao);

        String expectedMessage = "Requested resource not found (id = %s)".formatted(TAG_ID);
        assertThat(exception.getMessage()).isEqualTo(expectedMessage);
    }

    @Test
    void findAll() {
        List<Tag> expectedTags = new ArrayList<>();
        expectedTags.add(new Tag());

        when(tagDao.findAll()).thenReturn(expectedTags);

        List<Tag> actualTags = subject.findAll();

        verify(tagDao).findAll();
        verifyNoMoreInteractions(tagDao);

        assertThat(actualTags).isEqualTo(expectedTags);
    }

    @Test
    void create() {
        Tag newTag = new Tag();
        Tag expectedTag = new Tag();

        when(tagMapper.toTag(createRequest)).thenReturn(newTag);
        when(tagDao.save(any(Tag.class))).thenReturn(Optional.of(newTag));

        Tag actualTag = subject.create(createRequest);

        verify(tagDao).save(newTag);
        verify(tagMapper).toTag(createRequest);
        verifyNoMoreInteractions(tagDao);

        assertThat(actualTag).isEqualTo(expectedTag);
    }

    @Test
    void create_whenTagWithNameExists_throwsTagAlreadyExistsException() {
        Tag newTag = new Tag(TAG_ID, "tag new");

        when(tagMapper.toTag(createRequest)).thenReturn(newTag);
        when(createRequest.getName()).thenReturn(newTag.getName());

        TagAlreadyExistsException exception = assertThrows(TagAlreadyExistsException.class,
                () -> subject.create(createRequest));

        verify(tagDao).save(newTag);
        verify(tagMapper).toTag(createRequest);
        verifyNoMoreInteractions(tagDao);

        String expectedMessage = "Requested resource already exists (name = %s)".formatted(newTag.getName());
        assertThat(exception.getMessage()).isEqualTo(expectedMessage);
    }

    @Test
    void update() {
        Tag updateTag = new Tag(TAG_ID, "tag1");
        Tag expectedTag = new Tag(TAG_ID, "tag1");

        when(updateRequest.getName()).thenReturn("tag1");
        when(tagDao.findById(any(Long.class))).thenReturn(Optional.of(updateTag));
        when(tagDao.update(any(Tag.class))).thenReturn(updateTag);

        Tag actualTag = subject.update(updateRequest);

        verify(tagDao).update(updateTag);
        verifyNoMoreInteractions(tagDao);

        assertThat(actualTag).isEqualTo(expectedTag);
    }

    @Test
    void delete() {
        Tag deleteTag = new Tag(TAG_ID, "tag1");

        when(tagDao.findById(any(Long.class))).thenReturn(Optional.of(deleteTag));

        subject.delete(TAG_ID);

        verify(tagDao).delete(deleteTag);
        verifyNoMoreInteractions(tagDao);
    }

    @Test
    void findByName() {
        Tag expectedTag = new Tag();

        when(tagDao.findByName(any(String.class))).thenReturn(Optional.of(expectedTag));

        Tag actualTag = subject.findByName("tag1");

        verify(tagDao).findByName(any(String.class));
        verifyNoMoreInteractions(tagDao);

        assertThat(actualTag).isEqualTo(expectedTag);
    }

    @Test
    void findByName_whenTagWithNameNotExists_throwsTagNotFoundException() {
        Tag searchTag = new Tag(1L, "tag1");

        TagNotFoundException exception = assertThrows(TagNotFoundException.class,
                () -> subject.findByName(searchTag.getName()));

        verify(tagDao).findByName(any(String.class));
        verifyNoMoreInteractions(tagDao);

        String expectedMessage = "Requested resource not found (name = %s)".formatted(searchTag.getName());
        assertThat(exception.getMessage()).isEqualTo(expectedMessage);
    }

    @Test
    void createTagWithCheck_whenTagNotFound_returnsNewTag() {
        Tag newTag = new Tag(1L, "tag1");
        Tag expectedTag = new Tag(1L, "tag1");

        when(createRequest.getName()).thenReturn(newTag.getName());
        when(tagMapper.toTag(createRequest)).thenReturn(newTag);
        when(tagDao.save(any(Tag.class))).thenReturn(Optional.of(newTag));

        Tag actualTag = subject.createTagWithCheck(createRequest);

        verify(tagDao).findByName(newTag.getName());
        verify(tagMapper).toTag(createRequest);
        verify(tagDao).save(newTag);
        verifyNoMoreInteractions(tagDao);

        assertThat(actualTag).isEqualTo(expectedTag);
    }


    @Test
    void createTagWithCheck_whenTagFound_returnsCurrentTag() {
        Tag newTag = new Tag(1L, "tag1");
        Tag expectedTag = new Tag(1L, "tag1");

        when(createRequest.getName()).thenReturn(newTag.getName());
        when(tagDao.findByName(any(String.class))).thenReturn(Optional.of(expectedTag));

        Tag actualTag = subject.createTagWithCheck(createRequest);

        verify(tagDao).findByName(newTag.getName());
        verifyNoMoreInteractions(tagDao);

        assertThat(actualTag).isEqualTo(expectedTag);
    }
}