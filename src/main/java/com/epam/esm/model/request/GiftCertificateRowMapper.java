package com.epam.esm.model.request;

import com.epam.esm.model.entity.GiftCertificate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GiftCertificateRowMapper implements RowMapper<GiftCertificate> {

    public GiftCertificate mapRow(ResultSet resultSet, int i) throws SQLException {
        GiftCertificate entity = new GiftCertificate();
        entity.setId(resultSet.getLong("id"));
        entity.setName(resultSet.getString("name"));
        entity.setDescription(resultSet.getString("description"));
        entity.setPrice(resultSet.getDouble("price"));
        entity.setDuration(resultSet.getInt("duration"));
        entity.setCreateDate(resultSet.getTimestamp("create_date"));
        entity.setLastUpdateDate(resultSet.getTimestamp("last_update_date"));

        return entity;
    }
}
