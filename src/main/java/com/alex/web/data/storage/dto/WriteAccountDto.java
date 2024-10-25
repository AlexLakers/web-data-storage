package com.alex.web.data.storage.dto;


import lombok.Builder;
import lombok.Value;

/**
 * This class is DTO for the interaction process between {@link com.alex.web.data.storage.servlet.RegistrationServlet servlet}
 * and {@link com.alex.web.data.storage.service.AccountService service}.
 */

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
