package com.alex.web.data.storage.dto;

import com.alex.web.data.storage.entity.Account;
import jakarta.servlet.http.Part;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;

/**
 * This class is DTO for the interaction process between {@link com.alex.web.data.storage.servlet.UploadFileServlet servlet}
 * and {@link com.alex.web.data.storage.service.FileInfoService service}.
 */

@Value
@Builder
public class WriteFileInfoDto {
    LocalDate uploadDate;
    Part part;
    Account account;
}