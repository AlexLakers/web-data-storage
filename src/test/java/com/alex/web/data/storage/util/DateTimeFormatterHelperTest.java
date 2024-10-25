package com.alex.web.data.storage.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.stream.Stream;

@ExtendWith(MockitoExtension.class)
class DateTimeFormatterHelperTest {
    private final static String DATE_FORMAT = "yyyy-MM-dd";

    @Test
    void parseDate_shouldReturnDate_whenStringDateIsValid() {
        String stringDateValid = "1993-01-01";
        LocalDate expected = LocalDate.parse("1993-01-01", DateTimeFormatter.ofPattern(DATE_FORMAT));

        LocalDate actual = DateTimeFormatterHelper.parseDate(stringDateValid);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void parseDate_shouldThrowDateTimeParseException_whenStringDateIsInvalid() {
        String stringDateInvalid = "1993-01-01 00:00:00";
        Assertions.assertThrows(DateTimeParseException.class, () -> DateTimeFormatterHelper.parseDate(stringDateInvalid));
    }

    @ParameterizedTest
    @MethodSource("getArguments")
    void isValid_shouldReturnTrue_whenStringDateIsValid(String stringDate, boolean expected) {
        boolean actual = DateTimeFormatterHelper.isValid(stringDate);

        Assertions.assertEquals(expected, actual);
    }

    static Stream<Arguments> getArguments() {
        return Stream.of(
                Arguments.of("1993-01-01", true),
                Arguments.of("2000-02-02", true),
                Arguments.of("2024-07-07", true),
                Arguments.of("2024-08-08", true),
                Arguments.of("1993-01-01 11:11:11", false)
        );
    }

    @ParameterizedTest
    @NullAndEmptySource
    void isValid_shouldReturnFalse_whenStringDateIsEmptyOrNull(String stringDate) {
        boolean expected = false;

        boolean actual = DateTimeFormatterHelper.isValid(stringDate);

        Assertions.assertEquals(expected, actual);
    }
}