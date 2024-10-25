package com.alex.web.data.storage.validator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class ValidationResultTest {
    private ValidationResult validationResult;

    @BeforeEach
    void setUp() {
        validationResult = new ValidationResult();
    }

    @Test
    void hasErrors_shouldReturnTrue_whenErrorListIsNotEmpty() {
        validationResult.addError(new Error("123","An important error"));

        boolean actual=validationResult.hasErrors();

        assertTrue(actual);
    }

    @Test
    void hasErrors_shouldReturnFalse_whenErrorListIsEmpty() {

        boolean actual=validationResult.hasErrors();

        assertFalse(actual);
    }
}