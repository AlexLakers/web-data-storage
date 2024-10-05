package com.alex.web.data.storage.validator;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * This enum contains the constants for init all the {@link Error errors} in {@link ValidationResult validationResult}
 */

@RequiredArgsConstructor
@Getter
enum ErrorsDesc {
    LOGIN("login","111"),
    PASSWORD("password","222"),
    BIRTH_DATE("birthDate","333"),
    FIRST_NAME("firstName","444"),
    LAST_NAME("lastName","555"),
    ROLE("role","666"),
    LIMIT("limit","777"),
    FILE_NAME("fileName","888"),
    FILE_SIZE("fileSize","999"),
    FILE_CREATE_DATE("fileCreatingDate","000"),
    FOLDER_NAME("folderName","789");

    private final String errorCode;
    private final String errorName;
    private final static String ERROR_MESSAGE="The %s is incorrect";

    String getErrorDesc(){
        return ERROR_MESSAGE.formatted(getErrorName());
    }


}
