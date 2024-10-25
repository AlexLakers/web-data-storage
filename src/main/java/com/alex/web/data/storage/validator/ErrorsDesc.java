package com.alex.web.data.storage.validator;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * This enum contains the constants for init all the {@link Error errors} in {@link ValidationResult validationResult}
 */

@RequiredArgsConstructor
@Getter
enum ErrorsDesc {
    LOGIN("111","login"),
    PASSWORD("222","password"),
    BIRTH_DATE("333","birthDate"),
    FIRST_NAME("444","firstName"),
    LAST_NAME("555","lastName"),
    ROLE("666","role"),
    LIMIT("777","limit"),
    FILE_NAME("888","fileName"),
    FILE_SIZE("999","fileSize"),
    FILE_CREATE_DATE("000","fileCreatingDate"),
    FOLDER_NAME("789","folderName");

    private final String errorCode;
    private final String errorName;
    private final static String ERROR_MESSAGE="The %s is incorrect";

    String getErrorDesc(){
        return ERROR_MESSAGE.formatted(getErrorName());
    }


}
