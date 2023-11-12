package com.dentalmoovi.website.integration;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.sql.Connection;

import javax.sql.DataSource;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class DatabaseConnectionTest {
    @Autowired
    private DataSource dataSource;

    @Test
    void testDatabaseConnection() throws Exception {
        try (Connection connection = dataSource.getConnection()) {
            assertNotNull(connection);
        }
    }
} 