package com.alex.web.data.storage.validator;

import lombok.Builder;
import lombok.Value;

/**
 * This class describes error that's used in {@link ValidationResult validationResult}.
 * @see Validator, ErrorsDesc ,ValidationResult
 */

@Value
@Builder
public class Error {
    String code;
    String description;
}
