package com.epam.esm.dao.giftCertificate;

import com.epam.esm.dao.CRUDDao;
import com.epam.esm.model.entity.GiftCertificate;
import com.epam.esm.model.request.GiftCertificateRowMapper;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class GiftCertificateDao implements CRUDDao<GiftCertificate, Long> {

    private final JdbcTemplate jdbcTemplate;
    private final String SQL_FIND_ALL_CERTIFICATES = "SELECT * FROM gift_certificate";
    private final String SQL_FIND_CERTIFICATE = SQL_FIND_ALL_CERTIFICATES + " WHERE id = ?";
    private final String SQL_UPDATE_CERTIFICATE = "UPDATE gift_certificate SET";
    private final String SQL_DELETE_CERTIFICATE = "DELETE FROM gift_certificate WHERE id = ?";

    @Autowired
    public GiftCertificateDao(HikariDataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource, false);
    }

    public Optional<GiftCertificate> findById(Long id) {
        try {
            GiftCertificate entity = jdbcTemplate.queryForObject(
                    SQL_FIND_CERTIFICATE,
                    new Object[]{id},
                    new GiftCertificateRowMapper());

            return Optional.of(entity);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public List<GiftCertificate> findAll() {
        List<GiftCertificate> entities = jdbcTemplate.query(
                SQL_FIND_ALL_CERTIFICATES,
                new GiftCertificateRowMapper());

        return entities;
    }

    public Optional<GiftCertificate> save(GiftCertificate entity) {
        try {
            final Map<String, Object> parameters = new HashMap<>();
            parameters.put("name", entity.getName());
            parameters.put("description", entity.getDescription());
            parameters.put("duration", entity.getDuration());
            parameters.put("price", entity.getPrice());

            String[] columns = parameters.keySet().toArray(new String[parameters.size()]);

            SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                    .withTableName("gift_certificate")
                    .usingColumns(columns)
                    .usingGeneratedKeyColumns("id");

            Number id = simpleJdbcInsert.executeAndReturnKey(parameters);

            GiftCertificate entityNew = this.findById(id.longValue()).get();

            return Optional.of(entityNew);
        } catch (DuplicateKeyException e) {
            return Optional.empty();
        }
    }

    public GiftCertificate update(GiftCertificate entity) {
        ArrayList<Object> objects = new ArrayList<>();
        StringBuilder query = new StringBuilder(SQL_UPDATE_CERTIFICATE);

        if (entity.getName() != null) {
            query.append(" name = ?,");
            objects.add(entity.getName());
        }
        if (entity.getDescription() != null) {
            query.append(" description = ?,");
            objects.add(entity.getDescription());
        }
        if (entity.getDuration() != null) {
            query.append(" duration = ?,");
            objects.add(entity.getDuration());
        }
        if (entity.getPrice() != null) {
            query.append(" price = ?,");
            objects.add(entity.getPrice());
        }
        query.deleteCharAt(query.length() - 1);
        query.append(" WHERE id = ?");

        objects.add(entity.getId());

        jdbcTemplate.update(
                query.toString(),
                objects.toArray());

        return entity;
    }

    public void delete(GiftCertificate entity) {
        jdbcTemplate.update(
                SQL_DELETE_CERTIFICATE,
                new Object[]{entity.getId()});
    }
}
