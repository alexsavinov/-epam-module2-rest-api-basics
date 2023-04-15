package com.epam.esm.dao.giftCertificateTag;

import com.epam.esm.model.dto.SearchRequest;
import com.epam.esm.model.entity.GiftCertificate;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.model.request.GiftCertificateRowMapper;
import com.epam.esm.model.request.TagRowMapper;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class GiftCertificateTagDao {

    private final JdbcTemplate jdbcTemplate;
    private final String SQL_WHERE = "WHERE ";
    private final String SQL_ORDER_BY = "ORDER BY ";
    private final String SQL_DESC = "DESC";
    private final String SQL_AND = "AND ";
    private final String SQL_COMMA = ", ";
    private final String SQL_SEARCH_PART_SELECT = "SELECT gc.* ";
    private final String SQL_SEARCH_PART_FROM = "FROM gift_certificate gc ";
    private final String SQL_SEARCH_PART_FROM_WITH_TAGS =
            "FROM gift_certificate_tag as c_t " +
                    "INNER JOIN gift_certificate gc on gc.id = c_t.id_cert " +
                    "INNER JOIN tag t on t.id = c_t.id_tag ";
    private final String SQL_FIND_CERTIFICATE_TAGS =
            "SELECT t.id, t.name FROM gift_certificate_tag as c_t " +
                    "LEFT JOIN tag as t ON c_t.id_tag = t.id WHERE c_t.id_cert = ?";
    private final String SQL_DELETE_ALL_TAGS_OF_CERTIFICATE =
            "DELETE FROM gift_certificate_tag WHERE id_cert = ?";

    @Autowired
    public GiftCertificateTagDao(HikariDataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource, false);
    }

    private String generateFindQuery(SearchRequest searchRequest) {
        StringBuilder query = new StringBuilder(SQL_SEARCH_PART_SELECT);

        if (searchRequest.getTag() == null) {
            query.append(SQL_SEARCH_PART_FROM);
        } else {
            query.append(SQL_SEARCH_PART_FROM_WITH_TAGS);
        }

        query.append(addConditionsToQuery(searchRequest));
        query.append(addOrdersToQuery(searchRequest));

        return query.toString();
    }

    private String addConditionsToQuery(SearchRequest searchRequest) {

        StringBuilder query = new StringBuilder();

        String certificateName = searchRequest.getName();
        String certificateDescription = searchRequest.getDescription();
        String tagName = searchRequest.getTag();

        if (tagName != null || certificateName != null || certificateDescription != null) {
            query.append(SQL_WHERE);

            query.append(addConditionToQuery(tagName, "t.name = ? "));
            query.append(addConditionToQuery(certificateName, "gc.name LIKE ? "));
            query.append(addConditionToQuery(certificateDescription, "gc.description LIKE ? "));

            query.delete(query.length() - SQL_AND.length(), query.length());
        }

        return query.toString();
    }

    private String addConditionToQuery(String fieldValue, String fieldExpression) {
        if (fieldValue != null) {
            return fieldExpression + SQL_AND;
        }

        return "";
    }

    private String addOrdersToQuery(SearchRequest searchRequest) {

        StringBuilder query = new StringBuilder();

        String sortByName = searchRequest.getSortByName();
        String sortByCreateDate = searchRequest.getSortByCreateDate();
        String sortByUpdateDate = searchRequest.getSortByUpdateDate();

        if (sortByName != null || sortByCreateDate != null || sortByUpdateDate != null) {
            query.append(SQL_ORDER_BY);

            query.append(addOrderToQuery(sortByName, "gc.name "));
            query.append(addOrderToQuery(sortByCreateDate, "gc.create_date "));
            query.append(addOrderToQuery(sortByUpdateDate, "gc.name "));

            query.delete(query.length() - SQL_COMMA.length(), query.length());
        }

        return query.toString();
    }

    private String addOrderToQuery(String fieldValue, String fieldExpression) {

        StringBuilder query = new StringBuilder();

        if (fieldValue != null) {
            query.append(fieldExpression);
            if (fieldValue.toUpperCase().equals(SQL_DESC)) {
                query.append(SQL_DESC);
            }
            query.append(SQL_COMMA);
        }

        return query.toString();
    }

    private Object[] generateFindParams(SearchRequest searchRequest) {

        String certificateName = searchRequest.getName();
        String certificateDescription = searchRequest.getDescription();
        String tagName = searchRequest.getTag();

        List<Object> params = new ArrayList<>();

        if (certificateName != null) {
            params.add(String.format("%%%s%%", certificateName));
        }
        if (certificateDescription != null) {
            params.add(String.format("%%%s%%", certificateDescription));
        }
        if (tagName != null) {
            params.add(tagName);
        }

        return params.toArray();
    }

    public List<GiftCertificate> findCertificateWithSearchParams(SearchRequest searchRequest) {

        String query = generateFindQuery(searchRequest);
        Object[] params = generateFindParams(searchRequest);

        List<GiftCertificate> entities = jdbcTemplate.query(
                query,
                params,
                new GiftCertificateRowMapper());

        return entities;
    }

    public void deleteLinkOfCertificateAndTags(Long certificateId) {
        jdbcTemplate.update(
                SQL_DELETE_ALL_TAGS_OF_CERTIFICATE,
                new Object[]{certificateId});
    }

    public void updateCertificateWithTag(Long certificateId, Long tagId) {

        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("gift_certificate_tag");

        final Map<String, Object> parameters = new HashMap<>();
        parameters.put("id_cert", certificateId);
        parameters.put("id_tag", tagId);

        simpleJdbcInsert.execute(parameters);
    }


    public List<Tag> findTagsByCertificateId(Long id) {

        List<Tag> entities = jdbcTemplate.query(
                SQL_FIND_CERTIFICATE_TAGS,
                new Object[]{id},
                new TagRowMapper());

        return entities;
    }
}
