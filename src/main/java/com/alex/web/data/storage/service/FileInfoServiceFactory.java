package com.alex.web.data.storage.service;


import com.alex.web.data.storage.dao.FileInfoDao;
import com.alex.web.data.storage.mapper.ReadFileInfoDtoMapper;
import com.alex.web.data.storage.mapper.WriteFileInfoDtoMapper;
import com.alex.web.data.storage.validator.FileFilterDtoValidator;
import lombok.experimental.UtilityClass;

/**
 * This class contains a factory method for building and creation
 * a new instance {@link FileInfoService fileInfoService} with all the dependencies.
 *
 * @see FileInfoDao FileInfoDao
 * @see FileFilterDtoValidator FileFilterDtoValidator
 * @see ReadFileInfoDtoMapper ReadFileInfoDtoMapper
 * @see WriteFileInfoDtoMapper WriteFileInfoDtoMapper
 */

@UtilityClass
public class FileInfoServiceFactory {

    public static FileInfoService getFileInfoService() {
        return new FileInfoService(FileInfoDao.getInstance(),
                FileFilterDtoValidator.getInstance(),
                ReadFileInfoDtoMapper.getInstance(),
                WriteFileInfoDtoMapper.getInstance());
    }
}