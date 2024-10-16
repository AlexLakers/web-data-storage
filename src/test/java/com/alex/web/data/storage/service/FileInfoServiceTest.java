package com.alex.web.data.storage.service;

import com.alex.web.data.storage.dao.FileInfoDao;
import com.alex.web.data.storage.dto.DeleteFileInfoDto;
import com.alex.web.data.storage.dto.FileFilterDto;
import com.alex.web.data.storage.dto.ReadFileInfoDto;
import com.alex.web.data.storage.dto.WriteFileInfoDto;
import com.alex.web.data.storage.entity.Account;
import com.alex.web.data.storage.entity.FileInfo;
import com.alex.web.data.storage.exception.ValidationException;
import com.alex.web.data.storage.mapper.ReadFileInfoDtoMapper;
import com.alex.web.data.storage.mapper.WriteFileInfoDtoMapper;
import com.alex.web.data.storage.util.ConnectionHelper;
import com.alex.web.data.storage.validator.Error;
import com.alex.web.data.storage.validator.FileFilterDtoValidator;
import com.alex.web.data.storage.validator.ValidationResult;
import jakarta.servlet.http.Part;
import lombok.Cleanup;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class FileInfoServiceTest {
    private final static String FILE_DESC = "DESC: The date of creation:[%1$s]; The size of file:[%2$d] bytes";
    private static final Long ID = 1L;
    private static Account account;
    private static FileInfo fileInfo;
    private static ReadFileInfoDto readFileInfoDto;
    private static FileFilterDto fileFilterDto;

    @BeforeAll
    static void setUp() {
        account = Account.builder()
                .id(1L)
                .firstName("Alex")
                .lastName("Lakers")
                .login("lakers393")
                .password("Lakers393")
                .birthDate(LocalDate.of(1993, 1, 1))
                .folder("MyFolder")
                .build();
        fileInfo = FileInfo.builder()
                .id(1L)
                .size(1024L)
                .uploadDate(LocalDate.of(1993, 1, 1))
                .name("SomeFile.jpg")
                .account(account)
                .build();
        readFileInfoDto = ReadFileInfoDto.builder()
                .id(1L)
                .desc(FILE_DESC.formatted("1993-01-01", 1024L))
                .name("SomeFile.jpg")
                .accountId(2L)
                .build();
        fileFilterDto = FileFilterDto.builder()
                .limit("10")
                .size("1024")
                .uploadDate("1993-01-01")
                .accountId("1")
                .build();
    }

    @Mock
    private FileInfoDao fileInfoDao;
    @Mock
    private FileFilterDtoValidator fileFilterDtoValidator;
    @Mock
    private ReadFileInfoDtoMapper readFileInfoDtoMapper;
    @Mock
    private WriteFileInfoDtoMapper writeFileInfoDtoMapper;
    @InjectMocks
    private FileInfoService fileInfoService;

    @Test
    void findAll_shouldReturnReadFileInfoDtoList_whenFileFilterDtoIsValid() {
        ValidationResult validationResult = new ValidationResult();
        List<ReadFileInfoDto> expected = Collections.singletonList(readFileInfoDto);
        List<FileInfo> fileInfoList = Collections.singletonList(fileInfo);
        @Cleanup MockedStatic<ConnectionHelper> connectionHelper = Mockito.mockStatic(ConnectionHelper.class);
        connectionHelper.when(ConnectionHelper::createConnection).thenReturn(null);
        Mockito.when(fileFilterDtoValidator.isValid(fileFilterDto)).thenReturn(validationResult);
        Mockito.when(fileInfoDao.findAll(null, fileFilterDto)).thenReturn(fileInfoList);
        Mockito.when(readFileInfoDtoMapper.map(fileInfo)).thenReturn(readFileInfoDto);

        List<ReadFileInfoDto> actual = fileInfoService.findAll(fileFilterDto);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void findAll_shouldThrowValidationException_whenFileFilterDtoIsNotValid() {
        var expected = Error.builder()
                .code("777")
                .description("error")
                .build();
        ValidationResult validationResult = new ValidationResult();
        validationResult.addError(expected);
        Mockito.when(fileFilterDtoValidator.isValid(fileFilterDto)).thenReturn(validationResult);

        ValidationException thrown = Assertions.assertThrows(ValidationException.class, () -> fileInfoService.findAll(fileFilterDto));
        Error actual = thrown.getErrors().get(0);

        Assertions.assertEquals(expected, actual);
        Mockito.verifyNoInteractions(fileInfoDao, readFileInfoDtoMapper);
    }

    @Test
    void uploadFile_shouldReturnReadFileInfoDto_whenFileWasSaved() throws Exception {
        ByteArrayInputStream bis = new ByteArrayInputStream(new byte[]{1, 2, 3, 4});
        var mockPart = Mockito.mock(Part.class);
        Mockito.when(mockPart.getSubmittedFileName()).thenReturn("test_file.txt");
        Mockito.when(mockPart.getInputStream()).thenReturn(bis);
        var expected = readFileInfoDto;
        var writeFileInfoDto = WriteFileInfoDto.builder()
                .account(account)
                .part(mockPart)
                .build();
        @Cleanup MockedStatic<ConnectionHelper> connectionHelper = Mockito.mockStatic(ConnectionHelper.class);
        connectionHelper.when(ConnectionHelper::createConnection).thenReturn(null);
        Mockito.when(writeFileInfoDtoMapper.map(writeFileInfoDto)).thenReturn(fileInfo);
        Mockito.when(fileInfoDao.save(null, fileInfo)).thenReturn(fileInfo);
        Mockito.when(readFileInfoDtoMapper.map(fileInfo)).thenReturn(expected);

        ReadFileInfoDto actual = fileInfoService.uploadFile(writeFileInfoDto);

        Mockito.verify(fileInfoDao, Mockito.times(1)).save(null, fileInfo);
        Mockito.verify(writeFileInfoDtoMapper, Mockito.times(1)).map(writeFileInfoDto);
        Mockito.verify(readFileInfoDtoMapper, Mockito.times(1)).map(fileInfo);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void deleteFile_shouldReturnTrue_whenTheFileIsNotExist() throws NullPointerException {
        var deleteFileInfoDto = DeleteFileInfoDto.builder()
                .id(String.valueOf(ID))
                .folder("Unknown_folder")
                .name("Unknown_file.exe")
                .build();
        Mockito.when(fileInfoDao.delete(Mockito.any(), Mockito.anyLong())).thenReturn(false);

        boolean actual = fileInfoService.deleteFile(deleteFileInfoDto);

        Mockito.verify(fileInfoDao).delete(Mockito.any(Connection.class), Mockito.anyLong());
        Assertions.assertTrue(actual);
    }
}