package com.alex.web.data.storage.validator;

import com.alex.web.data.storage.dto.WriteAccountDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class WriteAccountDtoValidatorTest {
    private final static WriteAccountDtoValidator writeAccountDtoValidator = WriteAccountDtoValidator.getInstance();

    @Test
    void isVaLid_shouldReturnTrue_whenAllDtoIsNotEmpty() {
        var writeAccountDto = WriteAccountDto.builder()
                .login("Lakers")
                .password("Lakers1234")
                .birthDate("1993-01-21")
                .firstName("Alex")
                .lastName("Lakers")
                .role("ADMIN")
                .folder("myFolder")
                .build();

        ValidationResult validationResult = writeAccountDtoValidator.isValid(writeAccountDto);
        boolean actual = validationResult.hasErrors();

        assertFalse(actual);
    }

    @Test
    void isValid_shouldReturnFalse_whenValidateNameReturnFalse() {
        String invalidName = "This is a very long name";
        var writeAccountDto = WriteAccountDto.builder()
                .login("Lakers")
                .firstName("Alex")
                .lastName(invalidName)
                .birthDate(invalidName)
                .role("ADMIN")
                .password("Lakers1234")
                .folder("myFolder")
                .build();

        ValidationResult validationResult = writeAccountDtoValidator.isValid(writeAccountDto);
        boolean actual = validationResult.hasErrors();

        assertTrue(actual);
    }

    @ParameterizedTest
    @NullAndEmptySource
    void isValid_shouldReturnFalse_whenNaneIsNullOrEmpty(String nullOrEmptyName) {
        var writeAccountDto = WriteAccountDto.builder()
                .login("Lakers")
                .firstName("Alex")
                .lastName(nullOrEmptyName)
                .birthDate(nullOrEmptyName)
                .role("ADMIN")
                .password("Lakers1234")
                .folder("myFolder")
                .build();

        ValidationResult validationResult = writeAccountDtoValidator.isValid(writeAccountDto);
        boolean actual = validationResult.hasErrors();

        assertTrue(actual);
    }

    @ParameterizedTest
    @ValueSource(strings = {"/myFolder", "\\myFolder"})
    void isValid_shouldReturnFalse_whenValidateFolderNameReturnFalse(String invalidFolderName) {
        var writeAccountDto = WriteAccountDto.builder()
                .login("Lakers")
                .firstName("Alex")
                .lastName("Lakers")
                .birthDate("1993-01-21")
                .role("ADMIN")
                .password("Lakers1234")
                .folder(invalidFolderName)
                .build();

        ValidationResult validationResult = writeAccountDtoValidator.isValid(writeAccountDto);
        boolean actual = validationResult.hasErrors();

        assertTrue(actual);
    }

    @ParameterizedTest
    @NullAndEmptySource
    void isValid_shouldReturnFalse_whenFolderNameIsNullOrEmpty(String nullOrEmptyFolderName) {
        var writeAccountDto = WriteAccountDto.builder()
                .login("Lakers")
                .firstName("Alex")
                .lastName("Lakers")
                .birthDate("1993-01-21")
                .role("ADMIN")
                .password("Lakers1234")
                .folder(nullOrEmptyFolderName)
                .build();

        ValidationResult validationResult = writeAccountDtoValidator.isValid(writeAccountDto);
        boolean actual = validationResult.hasErrors();

        assertTrue(actual);
    }

    @ParameterizedTest
    @ValueSource(strings = {"1993-01-01 00:00:00", "1993-01-01 00:00"})
    void isValid_shouldReturnFalse_whenMethodValidateBirthdayReturnFalse(String invalidDate) {
        var writeAccountDto = WriteAccountDto.builder()
                .login("Lakers")
                .firstName("Alex")
                .lastName("Lakers")
                .birthDate(invalidDate)
                .folder("myFolder")
                .role("MODERATOR")
                .password("Lakers1234")
                .build();

        ValidationResult validationResult = writeAccountDtoValidator.isValid(writeAccountDto);
        boolean actual = validationResult.hasErrors();
        assertTrue(actual);
    }

    @ParameterizedTest
    @NullAndEmptySource
    void isValid_shouldReturnFalse_whenDateIsNullOrEmpty(String nullOrEmptyDate) {
        var writeAccountDto = WriteAccountDto.builder()
                .login("Lakers")
                .firstName("Alex")
                .lastName("Lakers")
                .birthDate(nullOrEmptyDate)
                .folder("myFolder")
                .role("MODERATOR")
                .password("Lakers1234")
                .build();

        ValidationResult validationResult = writeAccountDtoValidator.isValid(writeAccountDto);
        boolean actual = validationResult.hasErrors();
        assertTrue(actual);
    }

    @ParameterizedTest
    @MethodSource("getRoleArguments")
    void isValid_shouldReturnTrue_whenMethodValidateRoleNameReturnFalse(String role, boolean expected) {
        var writeAccountDto = WriteAccountDto.builder()
                .login("Lakers")
                .firstName("Alex")
                .lastName("Lakers")
                .birthDate("1993-01-21")
                .folder("myFolder")
                .role(role)
                .password("Lakers1234")
                .build();

        ValidationResult validationResult = writeAccountDtoValidator.isValid(writeAccountDto);
        boolean actual = validationResult.hasErrors();
        assertEquals(expected, actual);
    }

    static Stream<Arguments> getRoleArguments() {
        return Stream.of(
                Arguments.of("ADMIN", false),
                Arguments.of("USER", false),
                Arguments.of("MODERATOR", false),
                Arguments.of("INVALID_ROLE", true),
                Arguments.of(null,true)
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {"123456", "lakers1234", "lakers","1"})
    void isValid_shouldReturnFalse_whenMethodValidatePasswordReturnFalse(String invalidPassword) {

        var writeAccountDto = WriteAccountDto.builder()
                .login("lakers")
                .password(invalidPassword)
                .firstName("name")
                .lastName("surname")
                .birthDate("1993-01-21")
                .role("ADMIN")
                .folder("MyFolder")
                .build();

        ValidationResult validationResult = writeAccountDtoValidator.isValid(writeAccountDto);
        boolean actual = validationResult.hasErrors();

        assertTrue(actual);
    }

    @ParameterizedTest
    @NullAndEmptySource
    void isValid_shouldReturnFalse_whenMethodValidateLoginReturnFalse(String nullOrEmptyLogin) {
        var writeAccountDto = WriteAccountDto.builder()
                .login(nullOrEmptyLogin)
                .firstName("Alex")
                .lastName("Lakers")
                .birthDate("1993-01-21")
                .role("ADMIN")
                .password("Lakers1234")
                .folder("myFolder")
                .build();

        ValidationResult validationResult = writeAccountDtoValidator.isValid(writeAccountDto);
        boolean actual = validationResult.hasErrors();

        assertTrue(actual);
    }

    @ParameterizedTest
    @NullSource
    void isValid_shouldReturnFalse_whenLoginIsNullOrEmpty(String nullPassword){
        var writeAccountDto = WriteAccountDto.builder()
                .login("lakers")
                .password(nullPassword)
                .firstName("name")
                .lastName("surname")
                .birthDate("1993-01-21")
                .role("ADMIN")
                .folder("MyFolder")
                .build();

        ValidationResult validationResult = writeAccountDtoValidator.isValid(writeAccountDto);
        boolean actual = validationResult.hasErrors();

        assertTrue(actual);

    }
}