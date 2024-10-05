package com.alex.web.data.storage.validator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


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

        Assertions.assertTrue(actual);
    }

    @Test
    void hasErrors_shouldReturnFalse_whenErrorListIsEmpty() {

        boolean actual=validationResult.hasErrors();

        Assertions.assertFalse(actual);
    }
}