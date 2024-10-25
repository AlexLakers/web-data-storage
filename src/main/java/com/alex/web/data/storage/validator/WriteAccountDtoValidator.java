package com.alex.web.data.storage.validator;

import com.alex.web.data.storage.dto.WriteAccountDto;
import com.alex.web.data.storage.entity.RoleName;
import com.alex.web.data.storage.util.DateTimeFormatterHelper;
import lombok.extern.log4j.Log4j;

import java.time.LocalDate;
import java.util.Arrays;

/**
 * This class a specific implementation of {@link com.alex.web.data.storage.validator.Validator validator interface}.
 * You can use it to validate {@link com.alex.web.data.storage.dto.WriteAccountDto writeAccountDtoValidator}.
 *
 * @see ValidationResult
 */

@Log4j
public final class WriteAccountDtoValidator implements Validator<WriteAccountDto> {
    private static WriteAccountDtoValidator INSTANCE = new WriteAccountDtoValidator();

    public static WriteAccountDtoValidator getInstance() {
        return INSTANCE;
    }

    @Override
    public ValidationResult isValid(WriteAccountDto writeAccountDto) {
        log.debug("Validating writeAccountDto:{}" + writeAccountDto);
        ValidationResult validationResult = new ValidationResult();
        if (!validateName(writeAccountDto.getFirstName())) {
            validationResult.addError(Error.builder()
                    .code(ErrorsDesc.FIRST_NAME.getErrorCode())
                    .description(ErrorsDesc.FIRST_NAME.getErrorDesc())
                    .build());
        }
        if (!validateName(writeAccountDto.getLastName())) {
            validationResult.addError(Error.builder()
                    .code(ErrorsDesc.LAST_NAME.getErrorCode())
                    .description(ErrorsDesc.LAST_NAME.getErrorDesc())
                    .build());
        }
        if (!validateLogin(writeAccountDto.getLogin())) {
            validationResult.addError(Error.builder()
                    .code(ErrorsDesc.LOGIN.getErrorCode())
                    .description(ErrorsDesc.LOGIN.getErrorDesc())
                    .build());

        }
        if (!validatePassword(writeAccountDto.getPassword())) {
            validationResult.addError(Error.builder()
                    .code(ErrorsDesc.PASSWORD.getErrorCode())
                    .description(ErrorsDesc.PASSWORD.getErrorDesc())
                    .build());
        }
        if (!validateBirthDate(writeAccountDto.getBirthDate())) {
            validationResult.addError(Error.builder()
                    .code(ErrorsDesc.BIRTH_DATE.getErrorCode())
                    .description(ErrorsDesc.BIRTH_DATE.getErrorDesc())
                    .build());
        }
        if (!validateRoleName(writeAccountDto.getRole())) {
            validationResult.addError(Error.builder()
                    .code(ErrorsDesc.ROLE.getErrorCode())
                    .description(ErrorsDesc.ROLE.getErrorDesc())
                    .build());
        }
        if (!validateFolder(writeAccountDto.getFolder())) {
            validationResult.addError(Error.builder()
                    .code(ErrorsDesc.FOLDER_NAME.getErrorCode())
                    .description(ErrorsDesc.FOLDER_NAME.getErrorDesc())
                    .build()
            );
        }
        log.debug("The validation result:{%s}".formatted(validationResult));
        return validationResult;
    }

    private boolean validateLogin(String login) {
        return login != null &&
                !login.isEmpty();
    }

    private boolean validatePassword(String password) {
        return password != null &&
                password.matches("(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9]).{6,12}");
    }

    private boolean validateBirthDate(String birthDate) {
        return DateTimeFormatterHelper.isValid(birthDate) &&
                (DateTimeFormatterHelper.parseDate(birthDate).isAfter(LocalDate.of(1900, 1, 1)));
    }

    private boolean validateName(String name) {
        return name != null &&
                name.matches(".{1,16}");
    }

    private boolean validateRoleName(String roleName) {
        return Arrays.stream(RoleName.values())
                .anyMatch(role -> role.name().equals(roleName));
    }

    private boolean validateFolder(String folder) {
        return (folder != null) &&
                (!folder.isEmpty()) &&
                (!folder.contains("/")) &&
                (!folder.contains("\\"));
    }
}
