package com.alex.web.data.storage.dto;

import com.alex.web.data.storage.entity.Role;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;

/**
 * This class is DTO for the interaction process between {@link com.alex.web.data.storage.servlet.RegistrationServlet servlet}
 * and {@link com.alex.web.data.storage.service.AccountService service}.
 */

@Value
@Builder
public class ReadAccountDto {
    Long id;
    String firstName;
    String lastName;
    LocalDate birthDate;
    String login;
    Role role;
    String folder;
}
