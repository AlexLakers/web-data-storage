package com.alex.web.data.storage.validator;

import com.alex.web.data.storage.dto.FileFilterDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;

import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
class FileFilterDtoValidatorTest {
    static FileFilterDtoValidator fileFilterDtoValidator;

    @BeforeAll
    static void setUp() {
        fileFilterDtoValidator = FileFilterDtoValidator.getInstance();
    }

    @Test
    void isValid_shouldReturnTrue_whenAllDtoIsValid() {
        var fileFilterDto = FileFilterDto.builder()
                .limit("10")
                .size("1024")
                .uploadDate("2024-07-07")
                .build();

        ValidationResult validationResult = fileFilterDtoValidator.isValid(fileFilterDto);
        boolean actual = validationResult.hasErrors();

        Assertions.assertFalse(actual);
    }

    @Test
    void isValid_shouldReturnTrue_whenAllDtoIsEmpty() {
        var empty = "";
        var fileFilterDto = FileFilterDto.builder()
                .limit(empty)
                .size(empty)
                .uploadDate(empty)
                .build();
        ValidationResult validationResult = fileFilterDtoValidator.isValid(fileFilterDto);
        boolean actual = validationResult.hasErrors();

        Assertions.assertFalse(actual);
    }

    @ParameterizedTest
    @ValueSource(strings = {"0", "-1"})
    void isValid_shouldReturnFalse_whenSizeIsNegative(String invalidSize) {
        var fileFilterDto = FileFilterDto.builder()
                .size(invalidSize)
                .uploadDate("2024-07-07")
                .limit("20")
                .build();

        ValidationResult validationResult = fileFilterDtoValidator.isValid(fileFilterDto);
        boolean actual = validationResult.hasErrors();

        Assertions.assertTrue(actual);
    }

    @ParameterizedTest
    @ValueSource(strings = {"2024-07-07 00:00:00", "1993-01-01 13:13",})
    void isValid_shouldReturnFalse_whenUploadDateIsInvalid(String uploadDate) {

        var fileFilterDto = FileFilterDto.builder()
                .uploadDate(uploadDate)
                .limit("10")
                .size("1024")
                .build();

        ValidationResult validationResult = fileFilterDtoValidator.isValid(fileFilterDto);
        boolean actual = validationResult.hasErrors();

        Assertions.assertTrue(actual);
    }

    @ParameterizedTest
    @ValueSource(strings = {"0", "-1"})
    void isValid_shouldReturnFalse_whenLimitIsNegative(String invalidLimit) {
        var fileFilterDto = FileFilterDto.builder()
                .limit(invalidLimit)
                .size("1024")
                .uploadDate("1993-01-01")
                .build();

        ValidationResult validationResult = fileFilterDtoValidator.isValid(fileFilterDto);
        boolean actual = validationResult.hasErrors();

        Assertions.assertTrue(actual);
    }
}