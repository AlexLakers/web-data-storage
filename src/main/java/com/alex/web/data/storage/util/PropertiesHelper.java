package com.alex.web.data.storage.util;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import lombok.extern.log4j.Log4j;

import java.io.InputStream;
import java.util.Properties;

/**
 * This class include all the important methods for using properties from the 'application.properties'.
 * This file contains the following properties:the database connection, some directory as a storage,driver name.
 */

@Log4j
@UtilityClass
public class PropertiesHelper {
    private static Properties properties = new Properties();

    static {
        initProperties();
    }

    /**
     * Inits properties in the app. Loads all the properties from file 'application.properties'
     * to {@link Properties Properties}
     */

    @SneakyThrows
    private static void initProperties() {
        InputStream inputStream = PropertiesHelper.class.getClassLoader().getResourceAsStream("application.properties");

        properties.load(inputStream);
        log.info("The properties file:{%s} is loaded successfully".formatted(properties));
    }

    /**
     * Returns property value by transmitted key of the properties
     *
     * @param key key of properties
     * @return property value
     */

    public static String getProperty(String key) {
        log.debug("Get property by key:{%s}".formatted(key));
        if (key == null || key.isEmpty()) {
            log.error("The key:{%s} is null or empty".formatted(key));
            throw new IllegalArgumentException("The key is null or empty");
        }

        log.debug("The property value:{%1$s} by key:{%2$s}".formatted(properties.getProperty(key), key));
        return properties.getProperty(key);
    }
}
