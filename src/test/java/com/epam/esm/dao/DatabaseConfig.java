package com.epam.esm.dao;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DatabaseConfig  {
    private static HikariDataSource dataSource;
    private static HikariConfig config = new HikariConfig();
    private static Properties properties;

    public static HikariDataSource DataSource() throws IOException {

        properties = new Properties();

        Thread currentThread = Thread.currentThread();
        ClassLoader contextClassLoader = currentThread.getContextClassLoader();
        InputStream propertiesStream = contextClassLoader
                .getResourceAsStream("application.properties");
        properties.load(propertiesStream);

        config = new HikariConfig();

        config.setDriverClassName(properties.getProperty("jdbc.driver-class-name"));
        config.setJdbcUrl(properties.getProperty("jdbc.url"));
        config.setUsername(properties.getProperty("jdbc.username"));
        config.setPassword(properties.getProperty("jdbc.password"));

        dataSource = new HikariDataSource(config);

        return dataSource;
    }

}
