package com.alex.web.data.storage.util;


import lombok.*;
import lombok.experimental.UtilityClass;

import lombok.extern.log4j.Log4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import static lombok.AccessLevel.PRIVATE;

/**
 * This class contains all the necessary methods to provide connection with the database using connection parameters(url,login and pass).
 * These parameters store in the application.properties file.
 *
 * @see WrapperConnection wrapperConnection
 */

@UtilityClass
@Log4j
public class ConnectionHelper {

    /**
     * This enum contains all the available names of the connection properties.
     */

    @AllArgsConstructor(access = PRIVATE)
    @Getter(PRIVATE)
    private enum Key {
        URL("connection.url"),
        USERNAME("connection.username"),
        PASSWORD("connection.password"),
        DRIVER("connection.driver"),
        POOL_SIZE("connection.pool.size");
        private String value;
    }

    private static List<Connection> origPool = null;
    private static BlockingQueue<Connection> fakePool = null;

    static {
        initDriver();
        log.info("The driver:{%s} is loaded".formatted(PropertiesHelper.getProperty(Key.DRIVER.getValue())));

        initPool();
        log.info("The connection pool with size:{%d} is init".formatted(fakePool.size()));
    }

    @SneakyThrows
    private static Connection getConnection() {
        return DriverManager.getConnection(PropertiesHelper.getProperty(Key.URL.getValue()),
                PropertiesHelper.getProperty(Key.USERNAME.getValue()),
                PropertiesHelper.getProperty(Key.PASSWORD.getValue()));
    }

    /**
     * Returns one connection from the connection pool.
     *
     * @return Connection
     */

    @SneakyThrows
    public static Connection createConnection() {
        return fakePool.take();
    }

    @SneakyThrows
    private static void initDriver() {
        Class.forName(PropertiesHelper.getProperty(Key.DRIVER.getValue()));
    }

    /**
     * Init the connection pool.Connection pool include changed connection(method 'close')
     *
     * @see WrapperConnection wrapperConnection
     */

    private static void initPool() {
        origPool = new ArrayList<>();
        int poolSize = Integer.parseInt(PropertiesHelper.getProperty(Key.POOL_SIZE.getValue()));
        fakePool = new ArrayBlockingQueue<>(poolSize);

        for (int i = 0; i < poolSize; i++) {
            var connection = getConnection();
            origPool.add(connection);
            fakePool.add(new WrapperConnection(connection, fakePool));
        }
    }

    /**
     * Closes all the connection from the connection pool.
     */

    @SneakyThrows
    public static void closePool() {
        for (Connection connection : origPool) {
            connection.close();
        }
        log.info("The connection pool with size:{%d} is close".formatted(fakePool.size()));
    }
}
