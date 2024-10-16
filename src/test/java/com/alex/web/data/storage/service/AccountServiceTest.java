package com.alex.web.data.storage.service;

import com.alex.web.data.storage.dao.AccountDao;
import com.alex.web.data.storage.dao.RoleDao;
import com.alex.web.data.storage.dto.ReadAccountDto;
import com.alex.web.data.storage.dto.WriteAccountDto;
import com.alex.web.data.storage.entity.Account;
import com.alex.web.data.storage.entity.Role;
import com.alex.web.data.storage.entity.RoleName;
import com.alex.web.data.storage.exception.ValidationException;
import com.alex.web.data.storage.mapper.ReadAccountDtoMapper;
import com.alex.web.data.storage.mapper.WriteAccountDtoMapper;
import com.alex.web.data.storage.util.ConnectionHelper;
import com.alex.web.data.storage.validator.Error;
import com.alex.web.data.storage.validator.ValidationResult;
import com.alex.web.data.storage.validator.WriteAccountDtoValidator;
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

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;


@ExtendWith(MockitoExtension.class)
class AccountServiceTest {
    private final static Long ID = 1L;
    private static Account account;
    private static ReadAccountDto readAccountDto;
    private static WriteAccountDto writeAccountDto;
    private static Role role;

    @BeforeAll
    static void setUp() {
        account = Account.builder()
                .id(ID)
                .firstName("Alex")
                .lastName("Lakers")
                .login("lakers393")
                .password("Lakers393")
                .birthDate(LocalDate.of(1993, 1, 1))
                .folder("MyFolder")
                .build();
        role = Role.builder()
                .id(ID)
                .roleName(RoleName.ADMIN)
                .build();
        readAccountDto = ReadAccountDto.builder()
                .id(ID)
                .login("lakers3")
                .firstName("Alex")
                .lastName("Lakers")
                .birthDate(LocalDate.of(1993, 1, 1))
                .role(role)
                .folder("MyFolder")
                .build();
        writeAccountDto = WriteAccountDto.builder()
                .firstName("Alex")
                .lastName("Lakers")
                .login("lakers393")
                .password("Lakers393")
                .folder("MyFolder")
                .birthDate("1993-01-01")
                .role("ADMIN")
                .build();

    }

    @Mock
    private WriteAccountDtoValidator writeAccountDtoValidator;
    @Mock
    private AccountDao accountDao;
    @Mock
    private WriteAccountDtoMapper writeAccountDtoMapper;
    @Mock
    private ReadAccountDtoMapper readAccountDtoMapper;
    @Mock
    private RoleDao roleDao;
    @Mock
    private FileInfoService fileInfoService;
    @InjectMocks
    private AccountService accountService;

    @Test
    void createAccount_shouldReturnAccount_whenWriteAccountDtoIsValid() {
        ValidationResult validationResult = new ValidationResult();
        @Cleanup MockedStatic<ConnectionHelper> connectionHelper = Mockito.mockStatic(ConnectionHelper.class);
        connectionHelper.when(ConnectionHelper::createConnection).thenReturn(null);
        Mockito.when(writeAccountDtoValidator.isValid(writeAccountDto)).thenReturn(validationResult);
        Mockito.when(roleDao.findByName(null, writeAccountDto.getRole())).thenReturn(Optional.of(role));
        Mockito.when(writeAccountDtoMapper.map(writeAccountDto)).thenReturn(account);
        Mockito.when(accountDao.save(null, account)).thenReturn(account);
        Mockito.when(readAccountDtoMapper.map(account)).thenReturn(readAccountDto);
        var expected = readAccountDto;

        ReadAccountDto actual = accountService.createAccount(writeAccountDto);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void createAccount_shouldThrowValidationException_whenWriteAccountDtoIsNotValid() {
        var expected = Error.builder()
                .code("777")
                .description("error")
                .build();
        ValidationResult validationResult = new ValidationResult();
        validationResult.addError(expected);
        Mockito.when(writeAccountDtoValidator.isValid(writeAccountDto)).thenReturn(validationResult);

        ValidationException thrown = Assertions.assertThrows(ValidationException.class, () -> accountService.createAccount(writeAccountDto));
        Error actual = thrown.getErrors().get(0);

        Assertions.assertEquals(expected, actual);
        Mockito.verifyNoInteractions(roleDao, writeAccountDtoMapper, accountDao, fileInfoService);
    }

    @Test
    void login_shouldReturnReadAccountDto_whenLoginAndPasswordIsValid(){
        var expected = readAccountDto;
        @Cleanup MockedStatic<ConnectionHelper> connectionHelper = Mockito.mockStatic(ConnectionHelper.class);
        connectionHelper.when(ConnectionHelper::createConnection).thenReturn(null);
        Mockito.when(accountDao.findByLoginAndPassword(null, account.getLogin(), account.getPassword())).thenReturn(Optional.of(account));
        Mockito.when(readAccountDtoMapper.map(account)).thenReturn(expected);

        ReadAccountDto actual = accountService.login(account.getLogin(), account.getPassword()).get();

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void login_shouldReturnEmptyOptional_whenLoginAndPasswordIsNotValid() {
        @Cleanup MockedStatic<ConnectionHelper> connectionHelper = Mockito.mockStatic(ConnectionHelper.class);
        connectionHelper.when(ConnectionHelper::createConnection).thenReturn(null);
        Mockito.when(accountDao.findByLoginAndPassword(null, account.getLogin(), account.getPassword())).thenReturn(Optional.empty());
        Optional<ReadAccountDto> actual = accountService.login(account.getLogin(), account.getPassword());

        Assertions.assertTrue(actual.isEmpty());
        Mockito.verifyNoInteractions(readAccountDtoMapper);
    }


}