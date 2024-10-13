package com.alex.web.data.storage.dto;

import lombok.Builder;
import lombok.Value;


@Value
@Builder
public class DeleteFileInfoDto {
    String id;
    String name;
    String folder;
}