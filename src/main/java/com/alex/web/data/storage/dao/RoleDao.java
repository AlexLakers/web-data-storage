package com.alex.web.data.storage.dao;

import com.alex.web.data.storage.entity.Role;
import com.alex.web.data.storage.entity.RoleName;
import com.alex.web.data.storage.exception.DaoException;
import lombok.*;

import lombok.extern.log4j.Log4j;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static lombok.AccessLevel.*;

/**
 * This class describes a specific implementation of {@link Dao Dao interface}.You can use all the necessary functions
 * to interaction with the database 'files_repository',schema 'files_storage' and table 'role'.This table contains
 * all the necessary roles for account authorization process.
 */
//@Slf4j
@Log4j
@NoArgsConstructor(access = PRIVATE)
public final class RoleDao implements Dao<Long, Role> {
    private static final RoleDao INSTANCE = new RoleDao();

    public static RoleDao getInstance() {
        return INSTANCE;
    }

    @RequiredArgsConstructor(access = PRIVATE)
    private enum Sql {
        SAVE("""
                INSERT INTO files_storage.role(name)
                VALUES (?);
                    """),
        UPDATE("""
                UPDATE files_storage.role
                SET   name=?
                WHERE id=?;
                    """),
        DELETE("""
                DELETE FROM files_storage.role
                WHERE id=?;
                    """),
        FIND_ALL("""
                SELECT id,
                       name
                FROM files_storage.role
                    """),

        FIND_BY_ID("""
                SELECT id,
                       name
                FROM files_storage.role
                WHERE id=?;
                    """),
        FIND_BY_NAME("""
                SELECT id,
                       name
                FROM files_storage.role
                WHERE name=?;
                    """);
        private final String query;
    }

    /**
     * This is an override method of the {@link Dao#save(Connection, Object) save(connection,role)}
     * that saves the role entity into table 'role'.
     *
     * @param connection the database connection.
     * @param entity     a specific(transient) role entity
     * @return persistent role entity
     */

    @Override
    public Role save(Connection connection, Role entity) {
        log.debug("Save the role:{%s}".formatted(entity));
        try (var preparedStatement = connection.prepareStatement(Sql.SAVE.query, RETURN_GENERATED_KEYS)) {
            preparedStatement.setFetchSize(50);
            preparedStatement.setQueryTimeout(10);
            preparedStatement.setObject(1, entity.getRoleName().name());
            log.debug("The saving sql query:{%s}".formatted(preparedStatement));
            preparedStatement.executeUpdate();

            var resultSet = preparedStatement.getGeneratedKeys();

            if (resultSet.next()) {
                entity.setId(resultSet.getObject("id", Long.class));
                log.debug("Saved  role:{%s}".formatted(entity));
            }
            return entity;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    /**
     * This is an override method of the{@link Dao#update(Connection, Object) update(connection,role)}
     * that updates the role entity in the table 'role' by id.
     *
     * @param connection the database connection
     * @param entity     changed role entity
     * @return updated role entity
     */
    @Override
    public Role update(Connection connection, Role entity) {
        log.debug("Update the role:{%s}".formatted(entity));
        // try(var connection=ConnectionHelper.createConnection();
        try (var preparedStatement = connection.prepareStatement(Sql.UPDATE.query, RETURN_GENERATED_KEYS)) {
            preparedStatement.setFetchSize(50);
            preparedStatement.setQueryTimeout(10);
            preparedStatement.setObject(1, entity.getRoleName().name());
            preparedStatement.setObject(2, entity.getId());
            log.debug("The updating sql query for role:{%s}".formatted(preparedStatement));

            preparedStatement.executeUpdate();
            var resultSet = preparedStatement.getGeneratedKeys();

            if (resultSet.next()) {
                entity.setId(resultSet.getObject("id", Long.class));
                log.debug("Updated role:{%s}".formatted(entity));
            }
            return entity;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    /**
     * This is an override method of the{@link Dao#delete(Connection, Object) delete(connection,id)}
     * that delete the role entity from the table 'role' by id.
     *
     * @param key id of deleted role entity
     * @return boolean result of deleting process.If the role entity is deleted then result is true,else -false.
     */

    @Override
    public boolean delete(Connection connection, Long key) {
        log.debug("Delete the role by id={%d}".formatted(key));
        try (var preparedStatement = connection.prepareStatement(Sql.DELETE.query)) {
            preparedStatement.setObject(1, key);
            log.debug("Deleting sql query for role:{%s}".formatted(preparedStatement));
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    /**
     * This is an override method of the{@link Dao#findAll(Connection) findAll(connection)}
     * that find all the role entities in the table 'role'
     *
     * @param connection the database connection.
     * @return list of role entities.
     */
    @Override
    public List<Role> findAll(Connection connection) {
        log.debug("Find all the roles");
        try (var preparedStatement = connection.prepareStatement(Sql.FIND_ALL.query)) {
            preparedStatement.setFetchSize(50);
            preparedStatement.setQueryTimeout(10);
            log.debug("The search sql query for all the roles:{%s}".formatted(preparedStatement));
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Role> fileInfos = new ArrayList<>();
            while (resultSet.next()) {
                fileInfos.add(mapFrom(resultSet));
            }
            log.debug("The found roles:{%s}".formatted(fileInfos));
            return fileInfos;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    /**
     * This is an override method of the{@link Dao#findById(Connection, Object) findById(connection,id)}
     * that find the role entity in the table 'role' by id.
     *
     * @param connection the database connection.
     * @param key        id for search role entity in the table 'role'.
     * @return optional with role entity, if account entity is available in the table 'role ' by id,
     * else this method returns an empty optional.
     */
    @Override
    public Optional<Role> findById(Connection connection, Long key) {
        log.debug("Find the role by id:{%d}".formatted(key));
        try (var preparedStatement = connection.prepareStatement(Sql.FIND_BY_ID.query)) {
            preparedStatement.setFetchSize(50);
            preparedStatement.setQueryTimeout(10);
            preparedStatement.setObject(1, key);
            log.debug("The sql query for search the role by id:{%s}".formatted(preparedStatement));
            ResultSet resultSet = preparedStatement.executeQuery();
            Role role = null;
            if (resultSet.next()) {
                role = mapFrom(resultSet);
                log.debug("The found role:{%s}".formatted(role));
            }
            return Optional.ofNullable(role);

        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    /**
     * Returns {@link Optional optional} with role entity that's found by name.
     *
     * @param name name for search role entity in the table 'role'.
     * @return optional with role entity if entity is available in the table 'role' by name,
     * else this method returns an empty optional.
     */
    public Optional<Role> findByName(Connection connection, String name) {
        log.debug("Find the role by name:{%s}".formatted(name));
        try (var preparedStatement = connection.prepareStatement(Sql.FIND_BY_NAME.query)) {
            preparedStatement.setFetchSize(50);
            preparedStatement.setQueryTimeout(10);
            preparedStatement.setObject(1, name);
            log.debug("The sql query for search the role by name:{%s}".formatted(preparedStatement));
            ResultSet resultSet = preparedStatement.executeQuery();

            Role role = null;
            if (resultSet.next()) {
                role = mapFrom(resultSet);
                log.debug("The found role:{%s}".formatted(role));
            }
            return Optional.ofNullable(role);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @SneakyThrows
    private Role mapFrom(ResultSet resultSet) {
        return Role.builder()
                .id(resultSet.getObject("id", Long.class))
                .roleName(RoleName.valueOf(resultSet.getObject("name", String.class)))
                .build();
    }
}
