package com.alex.web.data.storage.dto;


import lombok.Builder;
import lombok.Value;

/**
 * This class is DTO-filter for search all the files using filtering.
 */

@Value
@Builder
public class FileFilterDto {
    String limit;
    String name;
    String size;
    String uploadDate;
    String accountId;
}