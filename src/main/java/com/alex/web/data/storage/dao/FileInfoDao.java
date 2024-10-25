package com.alex.web.data.storage.dao;

import com.alex.web.data.storage.dto.FileFilterDto;
import com.alex.web.data.storage.entity.FileInfo;
import com.alex.web.data.storage.exception.DaoException;
import lombok.*;

import lombok.extern.log4j.Log4j;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static lombok.AccessLevel.*;

/**
 * This class describes a specific implementation of {@link Dao Dao interface}.You can use all the necessary functions
 * to interaction with the database 'files_repository',schema 'files_storage' and table 'file_info'.This table contains
 * all the necessary information about files that belongs to the specific account.
 */
//@Slf4j
@Log4j
@NoArgsConstructor(access = PRIVATE)
public final class FileInfoDao implements Dao<Long, FileInfo> {
    private static final FileInfoDao INSTANCE = new FileInfoDao();

    public static FileInfoDao getInstance() {
        return INSTANCE;
    }

    private final AccountDao accountDao = AccountDao.getInstance();

    @RequiredArgsConstructor(access = PRIVATE)
    private enum Sql {
        SAVE("""
                    INSERT INTO files_storage.file_info(
                        name,
                        upload_date,
                        size,
                        account_id)
                    VALUES(?,?,?,?);
                """),
        UPDATE("""
                    UPDATE files_storage.file_info
                    SET name=?,upload_date=?,size=?,account_id=?
                    WHERE id=?;
                """),
        DELETE("""
                    DELETE FROM files_storage.file_info
                    WHERE id=?;
                """),
        FIND_ALL("""
                    SELECT  id,
                            name,
                            upload_date,
                            size,
                            account_id
                    FROM files_storage.file_info
                """),
        FIND_BY_ID("""
                    SELECT  id,
                            name,
                            upload_date,
                            size,
                            account_id
                    FROM files_storage.file_info
                    WHERE id=?;
                """),
        FIND_BY_NAME("""
                    SELECT  id,
                            name,
                            upload_date,
                            size,
                            account_id
                    FROM files_storage.file_info
                    WHERE name=?;
                """);

        private final String query;
    }

    /**
     * This is an override method of the {@link Dao#save(Connection, Object) save(connection,fileInfo)} that saves the fileInfo entity into table 'file_info'.
     *
     * @param connection the database connection.
     * @param entity     a specific(transient) fileInfo entity
     * @return persistent fileInfo entity.
     */
    @Override
    public FileInfo save(Connection connection, FileInfo entity) {
        log.debug("Save the fileInfo:{%s}".formatted(entity));

        try (var preparedStatement = connection.prepareStatement(Sql.SAVE.query, RETURN_GENERATED_KEYS)) {
            preparedStatement.setFetchSize(50);
            preparedStatement.setQueryTimeout(10);
            preparedStatement.setObject(1, entity.getName());
            preparedStatement.setObject(2, entity.getUploadDate());
            preparedStatement.setObject(3, entity.getSize());
            preparedStatement.setObject(4, entity.getAccount().getId());
            log.debug("The saving sql query for fileInfo:{%s}".formatted(preparedStatement));

            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();

            if (resultSet.next()) {
                entity.setId(resultSet.getObject("id", Long.class));
                log.debug("The fileInfo:{%s} is saved successfully".formatted(entity));
            }
            return entity;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    /**
     * This is an override method of the{@link Dao#update(Connection, Object) update(connection,fileInfo)} that updates the fileInfo entity in the table 'file_info' by id.
     *
     * @param connection the database connection
     * @param entity     changed fileInfo entity
     * @return updated fileInfo entity
     */
    @Override
    public FileInfo update(Connection connection, FileInfo entity) {
        log.debug("Update the fileInfo:{}" + entity);
        // try(var connection=ConnectionHelper.createConnection();
        try (var preparedStatement = connection.prepareStatement(Sql.UPDATE.query, RETURN_GENERATED_KEYS)) {
            preparedStatement.setFetchSize(50);
            preparedStatement.setQueryTimeout(10);
            preparedStatement.setObject(1, entity.getName());
            preparedStatement.setObject(2, entity.getUploadDate());
            preparedStatement.setObject(3, entity.getSize());
            preparedStatement.setObject(4, entity.getAccount().getId());
            preparedStatement.setObject(5, entity.getId());

            preparedStatement.executeUpdate();
            log.debug("The updating sql query for the fileInfo:{%s}".formatted(preparedStatement));
            ResultSet resultSet = preparedStatement.getGeneratedKeys();

            if (resultSet.next()) {
                entity.setId(resultSet.getObject("id", Long.class));
                log.debug("Updated fileInfo:{%s}".formatted(entity));
            }
            return entity;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    /**
     * This is an override method of the{@link Dao#delete(Connection connection, Object) delete(id) } that delete the fileInfo entity from the table 'file_info' by id.
     *
     * @param connection the database connection.
     * @param key        id of deleted fileInfo entity
     * @return boolean result of deleting process.If the fileInfo entity is deleted then result is true,else -false.
     */
    @Override
    public boolean delete(Connection connection, Long key) {
        log.debug("Delete the fileInfo by id={%d}".formatted(key));
        try (var preparedStatement = connection.prepareStatement(Sql.DELETE.query)) {
            preparedStatement.setFetchSize(50);
            preparedStatement.setQueryTimeout(10);
            preparedStatement.setObject(1, key);
            log.debug("Deleting sql query for fileInfo:{%s}".formatted(preparedStatement));
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    /**
     * This is an override method of the{@link Dao#findAll(Connection) findAll(connection)} that find all the fileInfo entities in the table 'file_info'
     *
     * @param connection the database connection.
     * @return list of fileInfo entities.
     */
    @Override

    public List<FileInfo> findAll(Connection connection) {
        log.debug("Find all the fileInfo");

        try (var preparedStatement = connection.prepareStatement(Sql.FIND_ALL.query)) {
            preparedStatement.setFetchSize(50);
            preparedStatement.setQueryTimeout(10);
            ResultSet resultSet = preparedStatement.executeQuery();
            log.debug("The search sql query for all the fileInfo:{%s}".formatted(preparedStatement));
            List<FileInfo> fileInfos = new ArrayList<>();
            while (resultSet.next()) {
                fileInfos.add(mapFrom(resultSet));
            }
            log.debug("The found List of fileInfo:{%s}".formatted(fileInfos));
            return fileInfos;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    /**
     * Returns list of fileInfo entity by {@link FileFilterDto filter}.Result contains all the fileInfo entities that are available in the table 'file_info'.
     *
     * @param filter filter for filtering process during search in the table 'file_info'.
     * @return list of fileInfo entities.
     */
    public List<FileInfo> findAll(Connection connection, FileFilterDto filter) {

        log.debug("Find all the fileInfo by filter:{%s}".formatted(filter));
        List<Object> parameters = new ArrayList<>();
        List<String> whereSqlConditions = new ArrayList<>();
        if ((filter.getUploadDate() != null) && !filter.getUploadDate().isEmpty()) {
            parameters.add(Date.valueOf(filter.getUploadDate()));
            whereSqlConditions.add("upload_date > ?");
        }
        if ((filter.getName() != null) && !filter.getName().isEmpty()) {
            parameters.add("%" + filter.getName() + "%");
            whereSqlConditions.add("name like ?");//Maybe '%?%'
        }
        if ((filter.getSize() != null) && !filter.getSize().isEmpty()) {
            parameters.add(Long.valueOf(filter.getSize()));
            whereSqlConditions.add("size > ?");
        }
        if ((filter.getAccountId() != null) && !filter.getAccountId().isEmpty()) {
            parameters.add(Long.valueOf(filter.getAccountId()));
            whereSqlConditions.add("account_id = ?");
        }
        String sqlByFilter = Sql.FIND_ALL.query.concat(whereSqlConditions.stream()
                .collect(Collectors.joining(" AND ", " WHERE ", " LIMIT ?")));

        Integer limit = ((filter.getLimit() != null) && !filter.getLimit().isEmpty())
                ? Integer.valueOf(filter.getLimit())
                : Integer.MAX_VALUE;

        parameters.add(limit);

        try (var preparedStatement = connection.prepareStatement(sqlByFilter)) {
            for (int i = 0; i < parameters.size(); i++) {
                preparedStatement.setObject(i + 1, parameters.get(i));
            }
            log.debug("The search sql query for all the files with filter:{%s}".formatted(preparedStatement));
            ResultSet resultSet = preparedStatement.executeQuery();
            List<FileInfo> files = new ArrayList<>();
            while (resultSet.next()) {
                files.add(mapFrom(resultSet));
            }
            log.debug("The found List of fileInfo:{%s}".formatted(files));
            return files;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    /**
     * This is an override method of the{@link Dao#findById(Connection, Object) findById(id)} that find the fileInfo entity in the table 'file_info' by id.
     *
     * @param connection the database connection.
     * @param key        id for search file_info entity in the table 'file_info'.
     * @return optional with role entity, if account entity is available in the table 'role ' by id,else this method returns an empty optional.
     */
    @Override
    public Optional<FileInfo> findById(Connection connection, Long key) {
        log.debug("Find the fileInfo by id:{%d}".formatted(key));

        try (var preparedStatement = connection.prepareStatement(Sql.FIND_BY_ID.query)) {
            preparedStatement.setObject(1, key);
            log.debug("The sql query for search the fileInfo by id:{%s}".formatted(preparedStatement));

            ResultSet resultSet = preparedStatement.executeQuery();
            FileInfo fileInfo = null;
            if (resultSet.next()) {
                fileInfo = mapFrom(resultSet);
                log.debug("The found fileInfo:{%s}".formatted(fileInfo));
            }
            return Optional.ofNullable(fileInfo);

        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    /**
     * Returns {@link Optional optional} with fileInfo entity that's found by name.
     *
     * @param connection the database connection
     * @param name       name for search fileInfo entity in the table 'file_info'.
     * @return optional with fileInfo entity if it is available in the table 'file_info' by name, else this method returns an empty optional.
     */

    public Optional<FileInfo> findByName(Connection connection, String name) {
        log.debug("Find the fileInfo by name:{%s}".formatted(name));
        try (var preparedStatement = connection.prepareStatement(Sql.FIND_BY_NAME.query)) {
            preparedStatement.setObject(1, name);
            log.debug("The sql query for search the fileInfo by name:{%s}".formatted(preparedStatement));

            ResultSet resultSet = preparedStatement.executeQuery();

            FileInfo fileInfo = null;
            if (resultSet.next()) {
                fileInfo = mapFrom(resultSet);
                log.debug("The found file:{%s}".formatted(fileInfo));
            }
            return Optional.ofNullable(fileInfo);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @SneakyThrows
    private FileInfo mapFrom(ResultSet resultSet) {

        Long accountId = resultSet.getObject("account_id", Long.class);
        return FileInfo.builder().id(resultSet.getObject("id", Long.class))
                .name(resultSet.getObject("name", String.class))
                .uploadDate(resultSet.getObject("upload_Date", Date.class).toLocalDate())
                .size(resultSet.getObject("size", Long.class))
                .account(accountDao.findById(resultSet.getStatement().getConnection(), accountId)
                        .orElseThrow(() -> new DaoException("The account not found by id=%d"
                                .formatted(accountId))))
                .build();
    }
}