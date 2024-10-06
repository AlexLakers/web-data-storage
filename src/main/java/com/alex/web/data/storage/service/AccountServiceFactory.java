package com.alex.web.data.storage.service;


import com.alex.web.data.storage.dao.AccountDao;
import com.alex.web.data.storage.dao.RoleDao;
import com.alex.web.data.storage.mapper.ReadAccountDtoMapper;
import com.alex.web.data.storage.mapper.WriteAccountDtoMapper;
import com.alex.web.data.storage.validator.WriteAccountDtoValidator;
import lombok.experimental.UtilityClass;

/**
 * This class contains a factory method for building and creation
 * a new instance {@link AccountService accountService} with all the dependencies.
 *
 * @see WriteAccountDtoValidator WriteAccountDtoValidator
 * @see AccountDao AccountDao
 * @see WriteAccountDtoMapper WriteAccountDtoMapper
 * @see ReadAccountDtoMapper ReadAccountDtoMapper
 * @see RoleDao RoleDao
 * @see FileInfoServiceFactory FileInfoSericeFactory
 */

@UtilityClass
public class AccountServiceFactory {

    public static AccountService getAccountService() {
        return new AccountService(WriteAccountDtoValidator.getInstance(),
                AccountDao.getInstance(),
                WriteAccountDtoMapper.getInstance(),
                ReadAccountDtoMapper.getInstance(),
                RoleDao.getInstance()
                //here file service
        );
    }
}