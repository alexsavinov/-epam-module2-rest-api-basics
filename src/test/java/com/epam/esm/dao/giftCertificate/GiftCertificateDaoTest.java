package com.epam.esm.dao.giftCertificate;

import com.epam.esm.dao.DatabaseConfig;
import com.epam.esm.model.entity.GiftCertificate;
import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.IOException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class GiftCertificateDaoTest {

    private static JdbcTemplate jdbcTemplate;
    private static GiftCertificateDao certificateDao;
    private static HikariDataSource dataSource;

    private static String SQL_DROP_TABLE_CERTIFICATE = "DROP TABLE IF EXISTS gift_certificate";
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
            "INSERT INTO gift_certificate (name, description, duration, price, create_date) " +
                    "VALUES ('cert1', 'desc1', 5, 2.2, '2023-04-14 17:00:00.11111 +00:00');" +
            "INSERT INTO gift_certificate (name, description, duration, price, create_date) " +
                    "VALUES ('cert2', 'desc2', 6, 3.3, '2023-04-15 17:00:00.11111 +00:00');" +
            "INSERT INTO gift_certificate (name, description, duration, price, create_date) " +
                    "VALUES ('cert3', 'desc3', 7, 4.5, '2023-04-16 17:00:00.11111 +00:00')";


    @BeforeAll
    public static void init() throws IOException {
        dataSource = DatabaseConfig.DataSource();

        certificateDao = new GiftCertificateDao(dataSource);

        jdbcTemplate = new JdbcTemplate(dataSource, false);
    }

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute(SQL_DROP_TABLE_CERTIFICATE);
        jdbcTemplate.execute(SQL_CREATE_TABLE_CERTIFICATE);
    }

    @Test
    void findById() {
        GiftCertificate expectedCertificate = GiftCertificate.builder()
                .id(1L)
                .name("cert1")
                .description("desc1")
                .duration(5)
                .price(2.2)
                .createDate(java.sql.Timestamp.valueOf("2023-04-14 20:00:00.11111"))
                .build();

        jdbcTemplate.execute(SQL_INSERT_CERTIFICATES);

        assertThat(certificateDao.findById(1L)).isEqualTo(Optional.of(expectedCertificate));
    }

    @Test
    void findAll() {
        jdbcTemplate.execute(SQL_INSERT_CERTIFICATES);

        assertThat(certificateDao.findAll().size()).isEqualTo(3);
    }

    @Test
    void save() {
        GiftCertificate certificateSave = GiftCertificate.builder()
                .id(1L)
                .name("cert1")
                .description("desc1")
                .duration(5)
                .price(2.2)
                .build();

        GiftCertificate expectedCertificate = GiftCertificate.builder()
                .id(1L)
                .name("cert1")
                .description("desc1")
                .duration(5)
                .price(2.2)
                .build();

        assertThat(certificateDao.save(certificateSave)).isEqualTo(Optional.of(expectedCertificate));
    }

    @Test
    void update() {
        GiftCertificate certificateUpdate = GiftCertificate.builder()
                .id(1L)
                .name("cert1111")
                .description("desc112")
                .duration(500)
                .price(2.2)
                .build();
        GiftCertificate expectedCertificate = GiftCertificate.builder()
                .id(1L)
                .name("cert1111")
                .description("desc112")
                .duration(500)
                .price(2.2)
                .build();

        jdbcTemplate.execute(SQL_INSERT_CERTIFICATES);

        assertThat(certificateDao.update(certificateUpdate)).isEqualTo(expectedCertificate);
    }

    @Test
    void delete() {
        GiftCertificate certificateDelete = GiftCertificate.builder().id(1L).build();

        jdbcTemplate.execute(SQL_INSERT_CERTIFICATES);

        certificateDao.delete(certificateDelete);

        assertThat(certificateDao.findById(1L)).isEqualTo(Optional.empty());
    }

    @AfterAll
    public static void cleanUp() {
        jdbcTemplate.execute(SQL_DROP_TABLE_CERTIFICATE);
    }
}