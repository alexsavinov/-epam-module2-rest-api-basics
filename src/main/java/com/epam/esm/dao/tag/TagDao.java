package com.epam.esm.dao.tag;

import com.epam.esm.dao.CRUDDao;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.model.request.TagRowMapper;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Component
public class TagDao implements CRUDDao<Tag, Long> {

    private final JdbcTemplate jdbcTemplate;
    private final String SQL_FIND_ALL_TAGS = "SELECT * FROM tag";
    private final String SQL_FIND_TAG_BY_ID = "SELECT * FROM tag WHERE id = ?";
    private final String SQL_FIND_TAG_BY_NAME = "SELECT * FROM tag WHERE name = ?";
    private final String SQL_DELETE_TAG = "DELETE FROM tag WHERE id = ?";
    private final String SQL_UPDATE_TAG = "UPDATE tag SET name = ? WHERE id = ?";

    @Autowired
    public TagDao(HikariDataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource, false);
    }

    public Optional<Tag> findById(Long id) {
        try {
            Tag entity = jdbcTemplate.queryForObject(
                    SQL_FIND_TAG_BY_ID,
                    new Object[]{id},
                    new TagRowMapper());

            return Optional.of(entity);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Optional<Tag> findByName(String name) {
        try {
            Tag entity = jdbcTemplate.queryForObject(
                    SQL_FIND_TAG_BY_NAME,
                    new Object[]{name},
                    new TagRowMapper());

            return Optional.of(entity);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public List<Tag> findAll() {
        List<Tag> entities = jdbcTemplate.query(
                SQL_FIND_ALL_TAGS,
                new TagRowMapper());

        return entities;
    }

    @Transactional
    public Optional<Tag> save(Tag entity) {
        try {
            SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                    .withTableName("tag")
                    .usingGeneratedKeyColumns("id");

            final Map<String, Object> parameters = new HashMap<>();
            parameters.put("name", entity.getName());

            Number id = simpleJdbcInsert.executeAndReturnKey(parameters);

            /* Looking for created entity and return */
            Tag entityNew = findById(id.longValue()).get();

            return Optional.of(entityNew);
        } catch (DuplicateKeyException e) {
            return Optional.empty();
        }
    }

    public Tag update(Tag entity) {
        jdbcTemplate.update(
                SQL_UPDATE_TAG,
                new Object[]{entity.getName(), entity.getId()});

        return entity;
    }

    public void delete(Tag entity) {
        jdbcTemplate.update(
                SQL_DELETE_TAG,
                new Object[]{entity.getId()});
    }
}
