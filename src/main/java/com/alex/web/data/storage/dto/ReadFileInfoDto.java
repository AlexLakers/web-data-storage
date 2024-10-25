package com.alex.web.data.storage.dto;

import lombok.Builder;
import lombok.Value;

/**
 * This class is DTO for the interaction process between servlets:{@link com.alex.web.data.storage.servlet.FileServlet
 * FileServlet},{@link com.alex.web.data.storage.servlet.UploadFileServlet UploadFileServlet}
 * and {@link com.alex.web.data.storage.service.FileInfoService service}.
 */

@Value
@Builder
public class ReadFileInfoDto {
    Long id;
    String name;
    String desc;
    Long accountId;
}