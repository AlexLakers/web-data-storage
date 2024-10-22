package com.alex.web.data.storage.dto;

import lombok.Builder;
import lombok.Value;

/**
 * This class is DTO for the interaction process between {@link com.alex.web.data.storage.servlet.DeleteFileServlet servlet}
 * and {@link com.alex.web.data.storage.service.FileInfoService service}.
 */

@Value
@Builder
public class DeleteFileInfoDto {
    String id;
    String name;
    String folder;
}