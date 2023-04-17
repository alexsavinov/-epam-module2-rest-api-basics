package com.epam.esm.dao.tag;

import com.epam.esm.dao.DatabaseConfig;
import com.epam.esm.model.entity.Tag;
import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.*;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class TagDaoTest {

    private static JdbcTemplate jdbcTemplate;
    private static TagDao tagDao;
    private static HikariDataSource dataSource;

    private static String SQL_DROP_TABLE_TAG =
            "DROP TABLE IF EXISTS tag";
    private static String SQL_CREATE_TABLE_TAG =
            "CREATE TABLE tag (" +
            "    id IDENTITY NOT NULL PRIMARY KEY," +
            "    name character varying(50) UNIQUE NOT NULL);";
    private static String SQL_INSERT_TAGS =
            "INSERT INTO tag (name) VALUES ('tag1');" +
            "INSERT INTO tag (name) VALUES ('tag2');" +
            "INSERT INTO tag (name) VALUES ('tag3');";

    @BeforeAll
    public static void init() throws IOException {
        dataSource = DatabaseConfig.DataSource();

        tagDao = new TagDao(dataSource);

        jdbcTemplate = new JdbcTemplate(dataSource, false);
    }

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute(SQL_DROP_TABLE_TAG);
        jdbcTemplate.execute(SQL_CREATE_TABLE_TAG);
    }

    @Test
    void findById() {
        Tag expectedTag = new Tag(1L, "tag1");

        jdbcTemplate.execute(SQL_INSERT_TAGS);

        assertThat(tagDao.findById(1L)).isEqualTo(Optional.of(expectedTag));
    }

    @Test
    void findByName() {
        Tag expectedTag = new Tag(1L, "tag1");

        jdbcTemplate.execute(SQL_INSERT_TAGS);

        assertThat(tagDao.findByName("tag1")).isEqualTo(Optional.of(expectedTag));
    }

    @Test
    void findByName_whenEmptyResultDataAccessException_returnsOptionalEmpty() {
        jdbcTemplate.execute(SQL_INSERT_TAGS);

        assertThat(tagDao.findByName("tag not exist")).isEqualTo(Optional.empty());
    }

    @Test
    void findAll() {
        jdbcTemplate.execute(SQL_INSERT_TAGS);

        assertThat(tagDao.findAll().size()).isEqualTo(3);
    }

    @Test
    void save() {
        Tag tagSave = Tag.builder().name("tag1").build();

        Tag expectedTag = new Tag(1L, "tag1");

        assertThat(tagDao.save(tagSave)).isEqualTo(Optional.of(expectedTag));
    }

    @Test
    void save_whenDuplicateKeyException_returnsOptionalEmpty() {
        Tag tagSave = Tag.builder().name("tag1").build();

        jdbcTemplate.execute(SQL_INSERT_TAGS);

        assertThat(tagDao.save(tagSave)).isEqualTo(Optional.empty());
    }

    @Test
    void update() {
        Tag tagUpdate = new Tag(1L, "tag1 new");
        Tag expectedTag = new Tag(1L, "tag1 new");

        jdbcTemplate.execute(SQL_INSERT_TAGS);

        assertThat(tagDao.update(tagUpdate)).isEqualTo(expectedTag);
    }

    @Test
    void delete() {
        Tag tagDelete = new Tag(1L, "tag1");

        jdbcTemplate.execute(SQL_INSERT_TAGS);

        tagDao.delete(tagDelete);

        assertThat(tagDao.findById(1L)).isEqualTo(Optional.empty());
    }

}