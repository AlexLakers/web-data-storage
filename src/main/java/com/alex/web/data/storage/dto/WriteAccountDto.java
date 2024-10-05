package com.alex.web.data.storage.dto;

import lombok.Builder;
import lombok.Value;


@Value
@Builder
public class WriteAccountDto {
    String firstName;
    String lastName;
    String birthDate;
    String login;
    String password;
    String role;
    String folder;
}
