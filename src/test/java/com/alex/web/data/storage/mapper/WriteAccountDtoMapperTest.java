package com.alex.web.data.storage.mapper;

import com.alex.web.data.storage.dto.WriteAccountDto;
import com.alex.web.data.storage.entity.Account;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class WriteAccountDtoMapperTest {

    @Test
    void mapFrom_shouldMapWriteAccountDtoToAccount_whenWriteAccountDtoIsFill() {
        var writeAccountDto = WriteAccountDto.builder()
                .firstName("Alex")
                .lastName("Lakers")
                .login("lakers393")
                .password("Lakers393")
                .folder("MyFolder")
                .birthDate("1993-01-01")
                .role("ADMIN")
                .build();
        var expected = Account.builder()
                .firstName("Alex")
                .lastName("Lakers")
                .login("lakers393")
                .password("Lakers393")
                .birthDate(LocalDate.of(1993, 1, 1))
                .folder("MyFolder")
                .build();

        Account actual = WriteAccountDtoMapper.getInstance().map(writeAccountDto);

        assertEquals(expected, actual);
    }
}