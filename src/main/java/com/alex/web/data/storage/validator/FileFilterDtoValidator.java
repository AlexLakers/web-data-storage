package com.alex.web.data.storage.validator;

import com.alex.web.data.storage.dto.FileFilterDto;
import com.alex.web.data.storage.util.DateTimeFormatterHelper;
import lombok.extern.log4j.Log4j;

import java.time.LocalDate;

/**
 * This class a specific implementation of {@link com.alex.web.data.storage.validator.Validator validator interface}.
 * You can use it to validate {@link com.alex.web.data.storage.dto.FileFilterDto fileFilterDto}.
 *
 * @see ValidationResult
 */

@Log4j
public class FileFilterDtoValidator implements Validator<FileFilterDto> {
    private static final FileFilterDtoValidator INSTANCE = new FileFilterDtoValidator();

    public static FileFilterDtoValidator getInstance() {
        return INSTANCE;
    }

    @Override
    public ValidationResult isValid(FileFilterDto fileFilterDto) {
        log.debug("Validating the fileFilterDto:{}" + fileFilterDto);
        ValidationResult validationResult = new ValidationResult();

        if (!fileFilterDto.getUploadDate().isEmpty() && !validateDate(fileFilterDto.getUploadDate())) {
            validationResult.addError(Error.builder()
                    .code(ErrorsDesc.FILE_CREATE_DATE.getErrorCode())
                    .description(ErrorsDesc.FILE_CREATE_DATE.getErrorDesc())
                    .build());
        }
        if (!fileFilterDto.getLimit().isEmpty() && !validateLimit(fileFilterDto.getLimit())) {
            validationResult.addError(Error.builder()
                    .code(ErrorsDesc.LIMIT.getErrorCode())
                    .description(ErrorsDesc.LIMIT.getErrorDesc())
                    .build()
            );
        }
        if (!fileFilterDto.getSize().isEmpty() && !validateSize(fileFilterDto.getSize())) {
            validationResult.addError(Error.builder()
                    .code(ErrorsDesc.FILE_SIZE.getErrorCode())
                    .description(ErrorsDesc.FILE_SIZE.getErrorDesc())
                    .build());
        }
        log.debug("The validation result:{%s}".formatted(validationResult));
        return validationResult;
    }

    private boolean validateSize(String size) {
        return Integer.parseInt(size) > 0;
    }

    private boolean validateDate(String creatingDate) {
        return DateTimeFormatterHelper.isValid(creatingDate) &&
                (DateTimeFormatterHelper.parseDate(creatingDate).isAfter(LocalDate.of(1900, 1, 1)));
    }

    private boolean validateLimit(String limit) {
        return Integer.parseInt(limit) > 0;
    }
}