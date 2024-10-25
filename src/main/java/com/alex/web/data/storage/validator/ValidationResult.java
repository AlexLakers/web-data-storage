package com.alex.web.data.storage.validator;

import java.util.ArrayList;
import java.util.List;

/**
 * Describes result of validation process.
 * @see Validator,Error, ErrorsDesc
 */

public class ValidationResult {
    private List<Error> errors = new ArrayList<>();
    public boolean hasErrors(){
        return !errors.isEmpty();
    }
    public List<Error> getErrors() {
        return errors;
    }
    public void addError(Error error){
        errors.add(error);
    }
}
