package com.alex.web.data.storage.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * This class is entity that is based on the one row from the table 'account'.
 * The database  is 'files_repository', the schema is 'files_storage'.
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account {
    private Long id;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private String login;
    private String password;
    private Role role;
    private String folder;

}