package com.alex.web.data.storage.mapper;

import com.alex.web.data.storage.dto.ReadAccountDto;
import com.alex.web.data.storage.entity.Account;
import com.alex.web.data.storage.entity.Role;
import com.alex.web.data.storage.entity.RoleName;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ReadAccountDtoMapperTest {

    @Test
    void mapFrom_shouldMapAccountToReadAccountDto_whenReadAccountDtoIsFill() {
        var account = Account.builder()
                .id(1L)
                .firstName("Alex")
                .lastName("Lakers")
                .login("lakers3")
                .password("Lakers393")
                .folder("MyFolder")
                .birthDate(LocalDate.of(1993, 1, 1))
                .role(Role.builder()
                        .roleName(RoleName.ADMIN)
                        .id(1L)
                        .build())
                .build();
        var expected = ReadAccountDto.builder()
                .id(1L)
                .login("lakers3")
                .firstName("Alex")
                .lastName("Lakers")
                .birthDate(LocalDate.of(1993, 1, 1))
                .role(Role.builder()
                        .roleName(RoleName.ADMIN)
                        .id(1L)
                        .build())
                .folder("MyFolder")
                .build();

        ReadAccountDto actual = ReadAccountDtoMapper.getInstance().map(account);

        assertEquals(expected, actual);
    }
}