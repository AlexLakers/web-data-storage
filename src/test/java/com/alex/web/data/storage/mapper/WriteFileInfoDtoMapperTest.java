package com.alex.web.data.storage.mapper;


import com.alex.web.data.storage.dto.WriteFileInfoDto;
import com.alex.web.data.storage.entity.Account;
import com.alex.web.data.storage.entity.FileInfo;
import com.alex.web.data.storage.entity.Role;
import com.alex.web.data.storage.entity.RoleName;
import jakarta.servlet.http.Part;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class WriteFileInfoDtoMapperTest {

    @Test
    void mapFrom_shouldMapToWriteFileInfoDtoToFileInfo_whenWriteFileInfoIsFill() {
        var partMock=Mockito.mock(Part.class);
        Mockito.when(partMock.getSubmittedFileName()).thenReturn("SomeFile.jpg");
        Mockito.when(partMock.getSize()).thenReturn(1024L);
        var writeFileInfoDto= WriteFileInfoDto.builder()
                .part(partMock)
                .uploadDate(LocalDate.of(2024,7,7))
                .account(buildAccount())
                .build();
        var expected= FileInfo.builder()
                .size(1024L)
                .uploadDate(LocalDate.of(2024,7,7))
                .name("SomeFile.jpg")
                .account(buildAccount())
                .build();

        FileInfo actual=WriteFileInfoDtoMapper.getInstance().map(writeFileInfoDto);

        assertEquals(expected, actual);
    }

    static Account buildAccount(){
        return Account.builder()
                .id(1L)
                .firstName("Alex")
                .lastName("Lakers")
                .login("lakers3")
                .password("Lakers393")
                .folder("MyFolder")
                .birthDate(LocalDate.of(2024,7,7))
                .role(Role.builder()
                        .roleName(RoleName.ADMIN)
                        .id(1L)
                        .build())
                .build();
    }
}