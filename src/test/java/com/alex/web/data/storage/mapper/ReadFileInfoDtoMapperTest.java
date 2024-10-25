package com.alex.web.data.storage.mapper;



import com.alex.web.data.storage.dto.ReadFileInfoDto;
import com.alex.web.data.storage.entity.Account;
import com.alex.web.data.storage.entity.FileInfo;
import com.alex.web.data.storage.entity.Role;
import com.alex.web.data.storage.entity.RoleName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ReadFileInfoDtoMapperTest {

    @Test
    void mapFrom_shouldMapFileInfoToReadFileInfoDto_whenFileInfoDtoIsFill() {
        final String FILE_DESC = "DESC: The date of creation:[%1$s]; The size of file:[%2$d] bytes";

        var fileInfo = FileInfo.builder()
                .id(1L)
                .size(1024L)
                .uploadDate(LocalDate.of(1993, 1, 1))
                .name("SomeFile.jpg")
                .account(Account.builder()
                        .id(2L)
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
                        .build())
                .build();
        var expected = ReadFileInfoDto.builder()
                .id(1L)
                .desc(FILE_DESC.formatted("1993-01-01", 1024L))
                .name("SomeFile.jpg")
                .accountId(2L)
                .build();

        ReadFileInfoDto actual = ReadFileInfoDtoMapper.getInstance().map(fileInfo);

        assertEquals(expected, actual);
    }
}