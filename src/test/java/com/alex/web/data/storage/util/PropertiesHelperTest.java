package com.alex.web.data.storage.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import java.util.stream.Stream;

class PropertiesHelperTest {

    @ParameterizedTest
    @MethodSource("getValidArguments")
    void getProperty_shouldReturnString_whenPropertyIsSet(String key, String expected) {
        String actual=PropertiesHelper.getProperty(key);

        Assertions.assertEquals(expected,actual);
    }
    static Stream<Arguments> getValidArguments(){
        return Stream.of(
                Arguments.of("connection.url","jdbc:h2:mem:default;lock_mode=0;DB_CLOSE_DELAY=-1"),
                Arguments.of("connection.username","sa"),
                Arguments.of("connection.pool.size","5"),
                Arguments.of("connection.driver","org.h2.Driver")
        );
    }

    @Test
    void getProperty_shouldReturnNull_whenPropertyIsNotAvailable() {
        String invalidKey="incorrect.Key";

        String actual=PropertiesHelper.getProperty(invalidKey);

        Assertions.assertNull(actual);
    }
    @ParameterizedTest
    @NullAndEmptySource
    void getProperty_shouldThrowNullPointerException_whenKeyIsNull(String key) {
        Assertions.assertThrows(IllegalArgumentException.class,()->PropertiesHelper.getProperty(key),"The key is null or empty");
    }
}