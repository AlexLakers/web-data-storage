package com.alex.web.data.storage.dao;

import static org.junit.jupiter.api.Assertions.*;

import com.alex.web.data.storage.entity.Account;
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

import static org.junit.jupiter.api.Assertions.*;

class AccountDaoIT extends BaseIntegrationTest {

    private final AccountDao accountDao=AccountDao.getInstance();
    private final RoleDao roleDao=RoleDao.getInstance();
    private static Role role;

    @BeforeAll
    static void setRole(){
        role=Role.builder()
                .id(1L)
                .roleName(RoleName.ADMIN)
                .build();
    }

    @Test
    void save_shouldReturnPersistAccount_whenAccountIsSaved()throws SQLException {
        @Cleanup var connection= ConnectionHelper.createConnection();
        Role savedRole=roleDao.save(connection,role);
        Account expected=buildAccountByRole(savedRole);
        Account savedAccount=accountDao.save(connection,buildAccountByRole(savedRole));
        // Long actual=savedAccount./*getId()*/;

        System.out.println(expected.getId()+" "+savedAccount.getId());
        Assertions.assertEquals(expected.getId(),savedAccount.getId());
    }



    @Test
    void update_shouldReturnUpdatedAccount_whenAccountIsUpdated()throws SQLException {
        @Cleanup var connection=ConnectionHelper.createConnection();
        Role savedRole=roleDao.save(connection,role);
        Account expected=accountDao.save(connection,buildAccountByRole(savedRole));
        expected.setLogin("UPDATED_LOGIN");

        Account actual=accountDao.update(connection,expected);

        Assertions.assertEquals(expected,actual);
    }

    @Test
    void delete_shouldReturnTrue_whenAccountIsDeleted()throws SQLException {
        @Cleanup var connection=ConnectionHelper.createConnection();
        Role savedRole=roleDao.save(connection,role);
        Account savedAccount=accountDao.save(connection,buildAccountByRole(savedRole));

        boolean actual=accountDao.delete(connection,savedAccount.getId());

        Assertions.assertTrue(actual);
    }

    @Test
    void findAll_shouldReturnAccountList_whenSomeAccountIsExist()throws SQLException {
        @Cleanup var connection=ConnectionHelper.createConnection();
        Role savedRole =roleDao.save(connection,role);
        Account expected=accountDao.save(connection,buildAccountByRole(savedRole));

        List<Account> actual=accountDao.findAll(connection);

        Assertions.assertFalse(actual.isEmpty());
        Assertions.assertEquals(expected,actual.get(0));

    }

    @Test
    void findByLoginAndPassword_shouldReturnAccount_whenAccountIsExist()throws SQLException {
        @Cleanup var connection=ConnectionHelper.createConnection();
        Role savedRole=roleDao.save(connection,role);
        Account expected=accountDao.save(connection,buildAccountByRole(savedRole));
        Optional<Account> actual=accountDao.findByLoginAndPassword( ConnectionHelper.createConnection(),expected.getLogin(), expected.getPassword());

        Assertions.assertTrue(actual.isPresent());
        Assertions.assertEquals(expected,actual.get());
    }
    @Test
    void findById_shouldReturnEmptyOptional_whenAccountIsNotExist()throws SQLException {
        @Cleanup var connection=ConnectionHelper.createConnection();
        Optional<Account> actual=accountDao.findById(connection,999999L);

        Assertions.assertTrue(actual.isEmpty());
    }
    @Test
    void findById_shouldReturnAccount_whenAccountIsExist()throws SQLException {
        @Cleanup var connection=ConnectionHelper.createConnection();
        Role savedRole=roleDao.save(connection,role);
        Account savedAccount=accountDao.save(connection,buildAccountByRole(savedRole));
        Long expected=savedAccount.getId();
        Optional<Account> actual=accountDao.findById(connection,expected);

        Assertions.assertTrue(actual.isPresent());
        Assertions.assertEquals(expected,actual.get().getId());

    }
    @Test
    void findByLoginAndPassword_shouldReturnEmptyAccount_whenAccountIsNotExist()throws SQLException{
        @Cleanup var connection=ConnectionHelper.createConnection();

        Optional<Account> actual=accountDao.findByLoginAndPassword(connection,"Unknown login","Unknown pass");
        Assertions.assertFalse(actual.isPresent());

    }
    Account buildAccountByRole(Role role) {
        return Account.builder()
                .id(1L)
                .firstName("Alex")
                .lastName("Lakers")
                .login("lakers393")
                .password("Lakers393")
                .birthDate(LocalDate.of(1993,1,1))
                .folder("MyFolder")
                .role(role)
                .build();

    }
}