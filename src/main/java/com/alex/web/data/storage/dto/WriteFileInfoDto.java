package com.alex.web.data.storage.dto;

import com.alex.web.data.storage.entity.Account;
import jakarta.servlet.http.Part;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;


@Value
@Builder
public class WriteFileInfoDto {
    LocalDate uploadDate;
    Part part;
    Account account;
}
