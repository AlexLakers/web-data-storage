package com.alex.web.data.storage.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * This class is entity that is based on the one row from the table 'file_info'.
 * The database  is 'files_repository', the schema is 'files_storage'.
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FileInfo {
    private Long id;
    private String name;
    private Long size;
    private LocalDate uploadDate;
    private Account account;
}