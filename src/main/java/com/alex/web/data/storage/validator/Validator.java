package com.alex.web.data.storage.validator;

/**
 * This is Validator interface. It contains the main method to valid all the necessary dto parameters.
 *
 * @param <E> dto type for validation process.
 */

public interface Validator<E> {
    ValidationResult isValid(E e);
}
