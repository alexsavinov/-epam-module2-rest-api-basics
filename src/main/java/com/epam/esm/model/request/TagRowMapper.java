package com.epam.esm.model.request;

import com.epam.esm.model.entity.Tag;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TagRowMapper implements RowMapper<Tag> {

    public Tag mapRow(ResultSet resultSet, int i) throws SQLException {
        Tag entity = new Tag();
        entity.setId(resultSet.getLong("id"));
        entity.setName(resultSet.getString("name"));

        return entity;
    }
}
