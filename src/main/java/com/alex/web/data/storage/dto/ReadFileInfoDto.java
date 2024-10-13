package com.alex.web.data.storage.dto;

import lombok.Builder;
import lombok.Value;


@Value
@Builder
public class ReadFileInfoDto {
    Long id;
    String name;
    String desc;
    Long accountId;
}