package com.alex.web.data.storage.dao;

import static org.junit.jupiter.api.Assertions.*;

import com.alex.web.data.storage.entity.Account;
import com.alex.web.data.storage.entity.FileInfo;
import com.alex.web.data.storage.entity.Role;
import com.alex.web.data.storage.entity.RoleName;
import com.alex.web.data.storage.test.BaseIntegrationTest;
import com.alex.web.data.storage.util.ConnectionHelper;
import lombok.Cleanup;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

class FileInfoDaoIT extends BaseIntegrationTest {
    private final static FileInfoDao fileInfoDao = FileInfoDao.getInstance();
    private final static AccountDao accountDao = AccountDao.getInstance();
    private final static RoleDao roleDao = RoleDao.getInstance();
    private static Role role;

    @BeforeAll
    static void setUpRole() {
        role = Role.builder()
                .id(1L)
                .roleName(RoleName.ADMIN)
                .build();
    }

    @Test
    void save_shouldReturnPersistFileInfo_whenFileInfoIsSaved() throws SQLException {
        @Cleanup var connection = ConnectionHelper.createConnection();
        Role savedRole = roleDao.save(connection, role);
        Account savedAccount = accountDao.save(connection, buildAccount(savedRole));
        FileInfo expected = buildFileInfo(savedAccount);

        FileInfo actual = fileInfoDao.save(connection, buildFileInfo(savedAccount));

        Assertions.assertEquals(expected.getId(), actual.getId());
    }

    @Test
    void update_shouldReturnFileInfoWithChanges_whenFileInfoIsUpdated() throws SQLException {
        @Cleanup var connection = ConnectionHelper.createConnection();
        Role savedRole = roleDao.save(connection, role);
        Account savedAccount = accountDao.save(connection, buildAccount(savedRole));
        FileInfo expected = fileInfoDao.save(connection, buildFileInfo(savedAccount));
        expected.setName("UPDATED_NAME");

        FileInfo actual = fileInfoDao.update(connection, expected);

        Assertions.assertEquals(expected.getName(), actual.getName());
    }

    @Test
    void delete_shouldReturnTrue_whenFileInfoIsDeletedById() throws SQLException {
        @Cleanup var connection = ConnectionHelper.createConnection();
        Role savedRole = roleDao.save(connection, role);
        Account savedAccount = accountDao.save(connection, buildAccount(savedRole));
        FileInfo savedFileInfo = fileInfoDao.save(connection, buildFileInfo(savedAccount));

        boolean actual = fileInfoDao.delete(connection, savedFileInfo.getId());

        Assertions.assertTrue(actual);
    }


    @Test
    void findAll_shouldReturnFileInfoList_whenFIleInfoIsExist() throws SQLException {
        @Cleanup var connection = ConnectionHelper.createConnection();
        Role savedRole = roleDao.save(connection, role);
        Account savedAccount = accountDao.save(connection, buildAccount(savedRole));
        FileInfo expected = fileInfoDao.save(connection, buildFileInfo(savedAccount));

        List<FileInfo> fileInfoList = fileInfoDao.findAll(connection);
        FileInfo actual = fileInfoList.get(0);

        Assertions.assertFalse(fileInfoList.isEmpty());
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void findById_shouldReturnFileInfo_whenIdIsExist() throws SQLException {
        @Cleanup var connection = ConnectionHelper.createConnection();
        Role savedRole = roleDao.save(connection, role);
        Account savedAccount = accountDao.save(connection, buildAccount(savedRole));
        FileInfo expected = fileInfoDao.save(connection, buildFileInfo(savedAccount));

        Optional<FileInfo> actual = fileInfoDao.findById(connection, expected.getId());

        Assertions.assertTrue(actual.isPresent());
        Assertions.assertEquals(expected, actual.get());
    }

    @Test
    void findById_shouldOptionalEmpty_whenIdIsNotExist() throws SQLException {
        @Cleanup var connection = ConnectionHelper.createConnection();

        Optional<FileInfo> actual = fileInfoDao.findById(connection, 9999999L);

        Assertions.assertFalse(actual.isPresent());
    }

    @Test
    void findByName_shouldReturnFileInfo_whenNameIsExist() throws SQLException {
        @Cleanup var connection = ConnectionHelper.createConnection();
        Role savedRole = roleDao.save(connection, role);
        Account savedAccount = accountDao.save(connection, buildAccount(savedRole));
        FileInfo expected = fileInfoDao.save(connection, buildFileInfo(savedAccount));

        Optional<FileInfo> actual = fileInfoDao.findByName(connection, "SomeFile.jpg");

        Assertions.assertTrue(actual.isPresent());
        Assertions.assertEquals(expected, actual.get());
    }

    @Test
    void findByName_shouldOptionalEmpty_whenNameIsNotExist() throws SQLException {
        @Cleanup var connection = ConnectionHelper.createConnection();
        Optional<FileInfo> actual = fileInfoDao.findByName(connection, "Unknown name");

        Assertions.assertFalse(actual.isPresent());
    }

    FileInfo buildFileInfo(Account account) {
        return FileInfo.builder()
                .id(1L)
                .size(1024L)
                .uploadDate(LocalDate.of(1993, 1, 1))
                .name("SomeFile.jpg")
                .account(account)
                .build();
    }

    Account buildAccount(Role role) {
        return Account.builder()
                .id(1L)
                .firstName("Alex")
                .lastName("Lakers")
                .login("lakers393")
                .password("Lakers393")
                .birthDate(LocalDate.of(1993, 1, 1))
                .folder("MyFolder")
                .role(role)
                .build();
    }
}