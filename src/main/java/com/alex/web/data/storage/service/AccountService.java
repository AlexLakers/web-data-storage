package com.alex.web.data.storage.service;

import com.alex.web.data.storage.dao.AccountDao;
import com.alex.web.data.storage.dao.RoleDao;
import com.alex.web.data.storage.dto.ReadAccountDto;
import com.alex.web.data.storage.dto.WriteAccountDto;
import com.alex.web.data.storage.entity.Account;
import com.alex.web.data.storage.entity.Role;
import com.alex.web.data.storage.exception.DaoException;
import com.alex.web.data.storage.exception.ServiceException;
import com.alex.web.data.storage.exception.ValidationException;
import com.alex.web.data.storage.mapper.ReadAccountDtoMapper;
import com.alex.web.data.storage.mapper.WriteAccountDtoMapper;
import com.alex.web.data.storage.util.ConnectionHelper;
import com.alex.web.data.storage.validator.WriteAccountDtoValidator;
import lombok.RequiredArgsConstructor;

import lombok.extern.log4j.Log4j;

import java.sql.SQLException;
import java.util.Optional;

import static lombok.AccessLevel.*;


/**
 * This class is a Service. It includes the methods for registration and authentication processes
 * for new user in the web-server-storage app.
 * It uses the database connection(database 'file_repository',schema 'files',table 'account').
 */

@RequiredArgsConstructor()
@Log4j
public final class AccountService {
    private static final AccountService INSTANCE = AccountServiceFactory.getAccountService();

    public static AccountService getInstance() {
        return INSTANCE;
    }

    private final WriteAccountDtoValidator writeAccountDtoValidator;
    private final AccountDao accountDao;
    private final WriteAccountDtoMapper writeAccountDtoMapper;
    private final ReadAccountDtoMapper readAccountDtoMapper;
    private final RoleDao roleDao;
    private final FileInfoService fileInfoService;

    @RequiredArgsConstructor(access = PRIVATE)
    private enum Message {
        CREATING_ERROR("An error occurred while creating the account."),
        ROLE_ERROR("The role not found by name=%s"),
        LOG_IN_ERROR("An error occurred while logging in.");
        private final String message;
    }

    /**
     * Returns {@link ReadAccountDto readAccountDto} after the successful registration process
     * by a new user web-file-storage app with using {@link WriteAccountDto writeAccountDtoValidator}.
     * Then, performs mapping process using {@link ReadAccountDtoMapper readAccountDtomapper}
     * that converts an input dto to output dto:{@link ReadAccountDto readAccountDto} if an input dto is valid.
     * After it performs the saving process into the database using {@link AccountDao accountDao}.
     * If some errors was detected during this process then occurs closure connection pool
     * and throw {@link ServiceException serviceException}.
     *
     * @param writeAccountDto an input dto
     * @return an output dto
     */
    public ReadAccountDto createAccount(WriteAccountDto writeAccountDto) {
        log.debug("Creating account by writeAccountDto:{%s}".formatted(writeAccountDto));
        var validationResult = writeAccountDtoValidator.isValid(writeAccountDto);

        if (validationResult.hasErrors()) {
            throw new ValidationException(validationResult.getErrors());
        }

        Account saveAccount = null;
        try (var connection = ConnectionHelper.createConnection()) {
            Optional<Role> maybeRole = roleDao.findByName(connection, writeAccountDto.getRole());
            Account account = writeAccountDtoMapper.map(writeAccountDto);
            account.setRole(maybeRole.orElseThrow(
                    () -> new ServiceException(Message.ROLE_ERROR.message.formatted(writeAccountDto.getRole())))
            );
            saveAccount = accountDao.save(connection, account);
        } catch (SQLException | DaoException e) {
            throw new ServiceException(Message.CREATING_ERROR.message, e);
        }

        fileInfoService.createFolder(saveAccount.getFolder());
        log.debug("The account:{%1$s} by writeAccountDto:{%2$s} is created" + saveAccount + writeAccountDto);

        return readAccountDtoMapper.map(saveAccount);
    }

    /**
     * Returns {@link Optional<ReadAccountDto> optoinal<ReadAccpuntDto>}
     * after authentication process by login and password.
     * The main functional is a finding process by credentials in the database.
     * It's defined in the{@link AccountDao accountDao}
     * As you understand this method returns empty {@link  Optional optional}
     * if authentication attempt has been failed.
     * If some errors was detected during this process then occurs closure connection pool
     * and throw {@link ServiceException serviceException}.
     *
     * @param login    an entered login
     * @param password an entered password
     * @return an output dto that's wrapped using opportunities of {@link Optional Optional class}.
     * If this dto is null then result optional object is empty.
     */
    public Optional<ReadAccountDto> login(String login, String password) {

        log.debug("Login process by login:{%1$s} and pass:{%2$s}".formatted(login, password));
        Optional<Account> maybeLoginAccount;

        try (var connection = ConnectionHelper.createConnection()) {
            maybeLoginAccount = accountDao.findByLoginAndPassword(connection, login, password);
            var maybeReadAccountDto = maybeLoginAccount.map(readAccountDtoMapper::map);
            log.debug("The account:{%s} is found".formatted(maybeReadAccountDto));
            return maybeReadAccountDto;

        } catch (SQLException | DaoException e) {
            throw new ServiceException(Message.LOG_IN_ERROR.message, e);
        }


    }


}
