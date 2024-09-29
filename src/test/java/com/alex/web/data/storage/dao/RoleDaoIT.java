package com.alex.web.data.storage.dao;

import com.alex.web.data.storage.entity.Role;
import com.alex.web.data.storage.entity.RoleName;
import com.alex.web.data.storage.test.BaseIntegrationTest;
import com.alex.web.data.storage.util.ConnectionHelper;
import lombok.Cleanup;
import org.junit.jupiter.api.*;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;


class RoleDaoIT extends BaseIntegrationTest {
    private final RoleDao roleDao = RoleDao.getInstance();
    private static Role role;

    @BeforeAll
    static void setUp() {
        role = Role.builder()
                .roleName(RoleName.ADMIN)
                .build();
    }

    @Test
    void save_shouldPersistRole_whenRoleIsSaved() throws SQLException {
        @Cleanup var connection = ConnectionHelper.createConnection();
        var expected = role;

        var actual = roleDao.save(connection, expected);

        Assertions.assertTrue(actual.getId() > 0);
    }

    @Test
    void update_shouldReturnUpdatedRole_whenRoleIsUpdated() throws SQLException {
        @Cleanup var connection = ConnectionHelper.createConnection();
        Role savedRole = roleDao.save(connection, role);
        Optional<Role> foundRole = roleDao.findById(connection, savedRole.getId());
        foundRole.ifPresent((r) -> r.setRoleName(RoleName.USER));

        Role expected = roleDao.update(connection, foundRole.get());

        Assertions.assertEquals(expected, foundRole.get());
    }

    @Test
    void delete_shouldReturnTrue_whenRoleIsDeleted() throws SQLException {
        @Cleanup var connection = ConnectionHelper.createConnection();
        Role savedRole = roleDao.save(connection, role);

        boolean actual = roleDao.delete(connection, savedRole.getId());

        Assertions.assertTrue(actual);
    }

    @Test
    void findAll_shouldReturnRolesList_whenSomeRoleIsFound() throws SQLException {
        @Cleanup var connection = ConnectionHelper.createConnection();
        var expected = roleDao.save(connection, role);

        List<Role> actual = roleDao.findAll(connection);

        Assertions.assertFalse(actual.isEmpty());
        Assertions.assertEquals(expected, actual.get(0));
    }

    @Test
    void findById_shouldReturnNotEmptyOptional_whenRoleIsExist() throws SQLException {
        @Cleanup var connection = ConnectionHelper.createConnection();
        var savedRole = roleDao.save(connection, role);
        Long expected = savedRole.getId();

        Optional<Role> actual = roleDao.findById(connection, savedRole.getId());

        Assertions.assertTrue(actual.isPresent());

        Assertions.assertEquals(expected, actual.get().getId());
    }

    @Test
    void findById_shouldReturnEmptyOptional_whenRoleIsNotExist() throws SQLException {
        @Cleanup var connection = ConnectionHelper.createConnection();

        Optional<Role> actual = roleDao.findById(connection, role.getId());

        Assertions.assertFalse(actual.isPresent());
    }

    @Test
    void findByName_shouldReturnNotEmptyOptional_whenRoleIsExist() throws SQLException {
        @Cleanup var connection = ConnectionHelper.createConnection();
        var savedRole = roleDao.save(connection, role);
        String expected = savedRole.getRoleName().name();

        Optional<Role> actual = roleDao.findByName(connection, expected);

        Assertions.assertTrue(actual.isPresent());
        Assertions.assertEquals(expected, actual.get().getRoleName().name());
    }

    @Test
    void findByName_shouldReturnEmptyOptional_whenRoleIsNotExist() throws SQLException {
        @Cleanup var connection = ConnectionHelper.createConnection();

        Optional<Role> actual = roleDao.findByName(connection, role.getRoleName().name());

        Assertions.assertFalse(actual.isPresent());
    }
}