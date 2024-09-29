package com.alex.web.data.storage.test;

import com.alex.web.data.storage.util.ConnectionHelper;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import java.sql.SQLException;

/**
 * This class is an additional class for integration tests.It contains the methods for the database initialization and cleanup processes.
 */

public class BaseIntegrationTest {

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    enum Sql {
        INIT_DB_SQL("""
                CREATE SCHEMA IF NOT EXISTS files_storage;
                CREATE TABLE IF NOT EXISTS files_storage.role(
                        id LONG auto_increment PRIMARY KEY ,
                        name VARCHAR(128) NOT NULL UNIQUE
                );
                CREATE TABLE IF NOT EXISTS files_storage.account(
                        id  LONG auto_increment PRIMARY KEY,
                        first_name VARCHAR(128) NOT NULL,
                        last_name VARCHAR(128) NOT NULL,
                        birth_date DATE NOT NULL ,
                        login VARCHAR(128) NOT NULL UNIQUE,
                        password VARCHAR(128) NOT NULL,
                        role_id BIGINT NOT NULL REFERENCES role(id) ON UPDATE CASCADE ON DELETE CASCADE,
                        folder VARCHAR(128) NOT NULL UNIQUE
                );
                CREATE TABLE  IF NOT EXISTS files_storage.file_info(
                        id LONG auto_increment PRIMARY KEY ,
                        name VARCHAR(128) NOT NULL,
                        upload_date DATE not null,
                        size BIGINT NOT NULL ,
                        account_id BIGINT NOT NULL REFERENCES account(id) ON UPDATE CASCADE ON DELETE CASCADE
                );
                """),
        DELETE_DATA_SQL("""
                DELETE FROM files_storage.account;
                DELETE FROM files_storage.role;
                DELETE FROM files_storage.file_info;
                """),
        RESET_ID_SQL("""
                ALTER TABLE files_storage.account ALTER COLUMN id RESTART WITH 1;
                ALTER TABLE files_storage.role ALTER COLUMN id RESTART WITH 1;
                ALTER TABLE files_storage.file_info ALTER COLUMN id RESTART WITH 1;
                """);
        private String query;
    }

    @BeforeAll
    static void initializeDB() throws SQLException {
        try (var connection = ConnectionHelper.createConnection();
             var statement = connection.createStatement()) {
            statement.execute(Sql.INIT_DB_SQL.query);
        }
    }

    @BeforeEach
    void cleanUpDB() throws SQLException {
        try (var connection = ConnectionHelper.createConnection();
             var statement = connection.createStatement()) {
            statement.execute(Sql.DELETE_DATA_SQL.query);
            statement.execute(Sql.RESET_ID_SQL.query);
        }
    }
}
