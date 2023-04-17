package com.epam.esm.dao.giftCertificateTag;

import com.epam.esm.dao.DatabaseConfig;
import com.epam.esm.dao.giftCertificate.GiftCertificateDao;
import com.epam.esm.model.dto.SearchRequest;
import com.epam.esm.model.entity.GiftCertificate;
import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GiftCertificateTagDaoTest {

    private static JdbcTemplate jdbcTemplate;
    private static GiftCertificateTagDao certificateTagDao;
    private static HikariDataSource dataSource;
    @Mock
    private static SearchRequest searchRequest;

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

    private static String SQL_DROP_TABLE_CERTIFICATE =
            "DROP TABLE IF EXISTS gift_certificate";
    private static String SQL_CREATE_TABLE_CERTIFICATE =
            "CREATE TABLE gift_certificate (" +
                    "    id IDENTITY NOT NULL PRIMARY KEY," +
                    "    name character varying(50) NOT NULL," +
                    "    description character varying(100)," +
                    "    price numeric(15, 2) default 0," +
                    "    duration numeric(15) default 0," +
                    "    create_date timestamp," +
                    "    last_update_date timestamp)";
    private static String SQL_INSERT_CERTIFICATES =
            "INSERT INTO gift_certificate (name, description, duration, price, create_date, last_update_date) " +
                    "VALUES ('cert1', 'description1', 5, 2.2, '2023-04-14 17:00:00.11111', '2023-04-14 19:00:00.11111');" +
                    "INSERT INTO gift_certificate (name, description, duration, price, create_date, last_update_date) " +
                    "VALUES ('cert2', 'description2', 6, 3.3, '2023-04-15 17:00:00.11111', '2023-04-15 18:00:00.11111');" +
                    "INSERT INTO gift_certificate (name, description, duration, price, create_date, last_update_date) " +
                    "VALUES ('cert3', 'description3', 7, 4.5, '2023-04-16 17:00:00.11111', '2023-04-16 17:00:00.11111')";

    private static String SQL_DROP_TABLE_CERTIFICATE_TAG =
            "DROP TABLE IF EXISTS gift_certificate_tag";
    private static String SQL_CREATE_TABLE_CERTIFICATE_TAG =
            "CREATE TABLE IF NOT EXISTS gift_certificate_tag(" +
                    "id_cert integer NOT NULL," +
                    "id_tag integer NOT NULL," +
                    "CONSTRAINT gift_certificate_tag_pkey PRIMARY KEY (id_cert, id_tag)," +
                    "CONSTRAINT id_cert FOREIGN KEY (id_cert)" +
                    "    REFERENCES gift_certificate (id)" +
                    "    ON UPDATE NO ACTION" +
                    "    ON DELETE CASCADE," +
                    "CONSTRAINT id_tag FOREIGN KEY (id_tag)" +
                    "    REFERENCES tag (id)" +
                    "    ON UPDATE NO ACTION" +
                    "    ON DELETE CASCADE)";
    private static String SQL_INSERT_CERTIFICATES_TAGS =
            "INSERT INTO gift_certificate_tag (id_cert, id_tag) VALUES ('1', '1');" +
                    "INSERT INTO gift_certificate_tag (id_cert, id_tag) VALUES ('1', '2');" +
                    "INSERT INTO gift_certificate_tag (id_cert, id_tag) VALUES ('2', '2');" +
                    "INSERT INTO gift_certificate_tag (id_cert, id_tag) VALUES ('2', '3');" +
                    "INSERT INTO gift_certificate_tag (id_cert, id_tag) VALUES ('3', '3');";

    @BeforeAll
    public static void init() throws IOException {
        dataSource = DatabaseConfig.DataSource();

        certificateTagDao = new GiftCertificateTagDao(dataSource);

        jdbcTemplate = new JdbcTemplate(dataSource, false);
    }

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute(SQL_DROP_TABLE_CERTIFICATE_TAG);
        jdbcTemplate.execute(SQL_DROP_TABLE_CERTIFICATE);
        jdbcTemplate.execute(SQL_DROP_TABLE_TAG);

        jdbcTemplate.execute(SQL_CREATE_TABLE_TAG);
        jdbcTemplate.execute(SQL_CREATE_TABLE_CERTIFICATE);
        jdbcTemplate.execute(SQL_CREATE_TABLE_CERTIFICATE_TAG);

        jdbcTemplate.execute(SQL_INSERT_TAGS);
        jdbcTemplate.execute(SQL_INSERT_CERTIFICATES);
        jdbcTemplate.execute(SQL_INSERT_CERTIFICATES_TAGS);
    }

    @Test
    void findCertificateWithSearchParams() {
        when(searchRequest.getTag()).thenReturn("tag2");
        when(searchRequest.getName()).thenReturn("cert");
        when(searchRequest.getDescription()).thenReturn("description");
        when(searchRequest.getSortByName()).thenReturn("DESC");
        when(searchRequest.getSortByUpdateDate()).thenReturn("DESC");
        when(searchRequest.getSortByCreateDate()).thenReturn("DESC");

        assertThat(certificateTagDao.findCertificateWithSearchParams(searchRequest).size()).isEqualTo(2);
    }

    @Test
    void deleteLinkOfCertificateAndTags() {
        certificateTagDao.deleteLinkOfCertificateAndTags(1L);

        assertThat(certificateTagDao.findTagsByCertificateId(1L).size()).isEqualTo(0);
        assertThat(certificateTagDao.findTagsByCertificateId(2L).size()).isEqualTo(2);
    }

    @Test
    void updateCertificateWithTag() {
        certificateTagDao.updateCertificateWithTag(1L, 3L);

        assertThat(certificateTagDao.findTagsByCertificateId(1L).size()).isEqualTo(3);
    }

    @AfterAll
    public static void cleanUp() {
        jdbcTemplate.execute(SQL_DROP_TABLE_CERTIFICATE_TAG);
        jdbcTemplate.execute(SQL_DROP_TABLE_CERTIFICATE);
        jdbcTemplate.execute(SQL_DROP_TABLE_TAG);
    }
}