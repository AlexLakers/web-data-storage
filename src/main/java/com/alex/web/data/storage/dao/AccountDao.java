package com.alex.web.data.storage.dao;

import com.alex.web.data.storage.entity.Account;
import com.alex.web.data.storage.exception.DaoException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

/**
 * This class describes a specific implementation of {@link Dao Dao interface}.You can use all the necessary functions
 * to interaction with the database 'files_repository',schema 'files_storage' and table 'account'.This table contains
 * the main information about every single user that finished registration process successfully.
 */

@NoArgsConstructor(access = AccessLevel.PRIVATE)
//@Slf4j
@Log4j
public class AccountDao implements Dao<Long, Account> {

    private static final AccountDao INSTANCE = new AccountDao();

    public static AccountDao getInstance() {
        return INSTANCE;
    }

    /**
     * Contains all the sql queries for the table 'account'.
     */

    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    enum Sql {
        SAVE("""
                INSERT INTO files_storage.account(
                       first_name,
                       last_name,
                       birth_date,
                       login,
                       password,
                       role_id,
                       folder)
                VALUES(?,?,?,?,?,?,?);
                   """),
        UPDATE("""
                UPDATE files_storage.account
                SET first_name=?,last_name=?,birth_date=?,login=?,password=?,role_id=?,folder=?
                WHERE id=?;
                   """),
        DELETE("""
                DELETE FROM files_storage.account
                WHERE id=?;
                   """),
        FIND_ALL("""
                SELECT id,
                       first_name,
                       last_name,
                       birth_date,
                       login,
                       password,
                       role_id,
                       folder
                FROM files_storage.account
                   """),
        FIND_BY_ID("""
                SELECT id,
                       first_name,
                       last_name,
                       birth_date,
                       login,
                       password,
                       role_id,
                       folder
                FROM files_storage.account
                WHERE id=?;
                   """),
        FIND_BY_LOGIN_AND_PASSWORD("""
                SELECT id,
                        first_name,
                        last_name,
                        birth_date,
                        login,
                        password,
                        role_id,
                        folder
                FROM files_storage.account
                WHERE login=? AND password=?;
                   """);

        private final String querry;
    }

    private final RoleDao roleDao = RoleDao.getInstance();

    /**
     * This is an override method of the {@link Dao#save(Connection, Object) save(connection,account)}
     * that saves the account entity into table 'account'.
     *
     * @param entity a specific(transient) account entity
     * @return persistent account entity
     */

    @Override
    public Account save(Connection connection, Account entity) {
        log.debug("Save the account:{%s}".formatted(entity));
        try (var preparedStatement = connection.prepareStatement(Sql.SAVE.querry, RETURN_GENERATED_KEYS)) {
            preparedStatement.setFetchSize(50);
            preparedStatement.setQueryTimeout(10);
            preparedStatement.setObject(1, entity.getFirstName());
            preparedStatement.setObject(2, entity.getLastName());
            preparedStatement.setObject(3, Date.valueOf(entity.getBirthDate()));
            preparedStatement.setObject(4, entity.getLogin());
            preparedStatement.setObject(5, entity.getPassword());
            preparedStatement.setObject(6, entity.getRole().getId());
            preparedStatement.setObject(7, entity.getFolder());
            log.debug("The saving sql query for account:{%s}".formatted(preparedStatement));
            preparedStatement.executeUpdate();

            var resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                entity.setId(resultSet.getObject("id", Long.class));
                log.info("Saved account:{%s}".formatted(entity));
            }
            return entity;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    /**
     * This is an override method of the{@link Dao#update(Connection, Object) update(connection,account)}
     * that updates the account entity in the table 'account' by id.
     *
     * @param connection the database connection.
     * @param entity     changed account entity
     * @return updated account entity
     */

    @Override
    public Account update(Connection connection, Account entity) {
        log.debug("Update the account:{%s}".formatted(entity));
        try (var preparedStatement = connection.prepareStatement(Sql.UPDATE.querry, RETURN_GENERATED_KEYS)) {
            preparedStatement.setFetchSize(50);
            preparedStatement.setQueryTimeout(10);
            preparedStatement.setObject(1, entity.getFirstName());
            preparedStatement.setObject(2, entity.getLastName());
            preparedStatement.setObject(3, Date.valueOf(entity.getBirthDate()));
            preparedStatement.setObject(4, entity.getLogin());
            preparedStatement.setObject(5, entity.getPassword());
            preparedStatement.setObject(6, entity.getRole().getId());
            preparedStatement.setObject(7, entity.getFolder());
            preparedStatement.setObject(8, entity.getId());

            preparedStatement.executeUpdate();
            var resultSet = preparedStatement.getGeneratedKeys();
            log.debug("The updating sql query for account:{%s}".formatted(preparedStatement));
            if (resultSet.next()) {
                entity.setId(resultSet.getObject("id", Long.class));
                log.debug("Updated account:{%s}".formatted(entity));
            }
            return entity;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    /**
     * This is an override method of the{@link Dao#delete(Connection, Object) delete(connection,id)}
     * that delete the account entity from the table 'account' by id.
     *
     * @param key        id of deleted account entity
     * @param connection the database connection
     * @return boolean result of deleting process.If the account entity is deleted then result is true,else -false.
     */

    @Override
    public boolean delete(Connection connection, Long key) {
        log.debug("Delete the account by id={%d}".formatted(key));
        try (var preparedStatement = connection.prepareStatement(Sql.DELETE.querry)) {
            preparedStatement.setFetchSize(50);
            preparedStatement.setQueryTimeout(10);
            preparedStatement.setObject(1, key);
            log.debug("The deleting sql query for account:{%s}".formatted(preparedStatement));
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    /**
     * This is an override method of the{@link Dao#findAll(Connection) findAll(connection)}
     * that find all the account entities in the table 'account'
     *
     * @param connection the database connection.
     * @return list of account entities.
     */
    @Override
    public List<Account> findAll(Connection connection) {
        log.debug("Find all the accounts");
        try (var preparedStatement = connection.prepareStatement(Sql.FIND_ALL.querry)) {
            preparedStatement.setFetchSize(50);
            preparedStatement.setQueryTimeout(10);

            ResultSet resultSet = preparedStatement.executeQuery();
            log.debug("The search sql query for all the accounts:{%s}".formatted(preparedStatement));

            List<Account> accounts = new ArrayList<>();
            while (resultSet.next()) {
                accounts.add(map(resultSet));
            }
            log.debug("The found List of account:{%s}".formatted(accounts));
            return accounts;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    /**
     * Returns {@link Optional optional} with account entity that's found by password and login.
     *
     * @param connection the database connection.
     * @param login      login for search account entity in the table 'account'.
     * @param password   pass for search account entity in the table 'account'.
     * @return optional with account entity if entity is available in the table 'account' by password and login,
     * else this method returns an empty option.
     */

    public Optional<Account> findByLoginAndPassword(Connection connection, String login, String password) {
        log.debug("Find account by Login:{%1$s} and password:{%2$s}".formatted(login, password));

        try (var preparedStatement = connection.prepareStatement(Sql.FIND_BY_LOGIN_AND_PASSWORD.querry)) {
            preparedStatement.setFetchSize(50);
            preparedStatement.setQueryTimeout(10);
            preparedStatement.setObject(1, login);
            preparedStatement.setObject(2, password);

            var resultSet = preparedStatement.executeQuery();
            log.debug("The sql query for searching process the account by login and password:{%s}".formatted(preparedStatement));
            Account account = null;

            if (resultSet.next()) {
                account = map(resultSet);
                log.debug("The found account:{%s}".formatted(account));
            }
            return Optional.ofNullable(account);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    /**
     * This is an override method of the{@link Dao#findById(Connection, Object) findById(connection,id)}
     * that find the account entity in the table 'account' by id.
     *
     * @param connection the database connection.
     * @param key        id for search account entity in the table 'account'.
     * @return optional with account entity, if account entity is available in the table 'account ' by id,
     * else this method returns an empty optional.
     */

    @Override
    public Optional<Account> findById(Connection connection, Long key) {
        log.debug("Find the account by id:{%d}".formatted(key));

        try (var preparedStatement = connection.prepareStatement(Sql.FIND_BY_ID.querry)) {
            preparedStatement.setFetchSize(50);
            preparedStatement.setQueryTimeout(10);
            preparedStatement.setObject(1, key);
            log.debug("The sql query for search the account by id:{%s}".formatted(preparedStatement));
            ResultSet resultSet = preparedStatement.executeQuery();
            Account account = null;
            if (resultSet.next()) {
                account = map(resultSet);
                log.debug("The found account:{%s}".formatted(account));
            }
            return Optional.ofNullable(account);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @SneakyThrows
    private Account map(ResultSet resultSet) {

        Long roleId = resultSet.getObject("role_id", Long.class);

        return Account.builder().id(resultSet.getObject("id", Long.class))
                .firstName(resultSet.getObject("first_name", String.class))
                .lastName(resultSet.getObject("last_name", String.class))
                .birthDate(resultSet.getObject("birth_date", Date.class).toLocalDate())
                .login(resultSet.getObject("login", String.class))
                .password(resultSet.getObject("password", String.class))
                .role(roleDao.findById(resultSet.getStatement().getConnection(), roleId)
                        .orElseThrow(() -> new DaoException("The role not found by id=%d"
                                .formatted(roleId))))
                .folder(resultSet.getObject("folder", String.class))
                .build();
    }
}
