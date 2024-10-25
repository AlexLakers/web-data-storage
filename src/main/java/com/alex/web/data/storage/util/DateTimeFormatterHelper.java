package com.alex.web.data.storage.util;

import lombok.experimental.UtilityClass;
import lombok.extern.log4j.Log4j;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;

/**
 * This class includes all the necessary methods for parsing data from database to {@link LocalDate localDate}.
 */

@Log4j
@UtilityClass
public class DateTimeFormatterHelper {
    public static final String PATTERN = "yyyy-MM-dd";

    /**
     * Returns result of parsing from string data to {@link LocalDate date}.
     *
     * @param date a date as a string.
     * @return {@link LocalDate date} from transmitted string if is it possible.
     */

    public LocalDate parseDate(String date) {
        log.debug("Parse the date {%s}".formatted(date));
        return LocalDate.parse(date, DateTimeFormatter.ofPattern(PATTERN));
    }

    /**
     * Returns result of the following method:{@link #parseDate(String) parseDate(date)}
     *
     * @param date a date as a string
     * @return true if {@link #parseDate(String) parseDate(date)} return true, else-false.
     */

    public boolean isValid(String date) {
        try {
            log.debug("Validating the date {%s}".formatted(date));
            return Optional.ofNullable(parseDate(date)).isPresent();
        } catch (DateTimeParseException | NullPointerException e) {
            log.error("The date:{%1$s} is invalid:{%2$s}".formatted(date, e.getMessage()));
            return false;
        }
    }
}