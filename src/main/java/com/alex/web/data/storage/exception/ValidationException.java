package com.alex.web.data.storage.exception;

import java.util.List;

/**
 * This class describes exception which can happen during validation process.This class contains information
 * about all the validation errors.
 * @see Error Error
 */

public class ValidationException extends RuntimeException{
    private final List<Error> errors;
    public ValidationException(List<Error> errors){
        this.errors=errors;
    }
    public List<Error> getErrors() {
        return errors;
    }
}
