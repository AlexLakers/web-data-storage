package com.alex.web.data.storage.service;

import com.alex.web.data.storage.dao.FileInfoDao;
import com.alex.web.data.storage.dto.DeleteFileInfoDto;
import com.alex.web.data.storage.dto.FileFilterDto;
import com.alex.web.data.storage.dto.ReadFileInfoDto;
import com.alex.web.data.storage.dto.WriteFileInfoDto;
import com.alex.web.data.storage.entity.FileInfo;
import com.alex.web.data.storage.exception.DaoException;
import com.alex.web.data.storage.exception.ServiceException;
import com.alex.web.data.storage.exception.ValidationException;
import com.alex.web.data.storage.mapper.ReadFileInfoDtoMapper;
import com.alex.web.data.storage.mapper.WriteFileInfoDtoMapper;
import com.alex.web.data.storage.util.ConnectionHelper;
import com.alex.web.data.storage.util.PropertiesHelper;
import com.alex.web.data.storage.validator.FileFilterDtoValidator;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * This class is a Service. It includes the methods for working with a file for authenticated user.
 * It uses the database connection(database 'file_repository',schema 'files',table 'account').
 */

@Log4j
@RequiredArgsConstructor()
public final class FileInfoService {
    private final static String BASE_DIR = PropertiesHelper.getProperty("storage.base.dir");
    private static final int BUFFER_SIZE = 4096;
    private static final FileInfoService INSTANCE=FileInfoServiceFactory.getFileInfoService();

    public static FileInfoService getInstance() {
        return INSTANCE;
    }

    private final FileInfoDao fileInfoDao;
    private final FileFilterDtoValidator fileFilterDtoValidator;
    private final ReadFileInfoDtoMapper readFileInfoDtoMapper;
    private final WriteFileInfoDtoMapper writeFileInfoDtoMapper;

    @RequiredArgsConstructor
    private enum Message {
        SEARCH_ERROR("An error has been detected during search all files."),
        DELETING_ERROR("An error has been detected during deleting the file."),
        SAVING_ERROR("An error has been detected during saving the files.");
        private final String message;
    }

    /**
     * Returns all the files that belong to specific account in the web-file-storage app.
     * Firstly , occurs the validation process of input filter {@link FileFilterDto fileFilterDto}
     * with using {@link FileFilterDtoValidator fileFilterDtoValidator}.
     * After it performs the searching process in the database using {@link FileInfoDao fileInfoDao}.
     * If some errors was detected during this process then occurs closure connection pool
     * and throw {@link ServiceException serviceException}.
     *
     * @param fileFilterDto an input dto(filter)
     * @return list of output dto about file.
     */

    public List<ReadFileInfoDto> findAll(FileFilterDto fileFilterDto) {
        log.debug("Find all files from {%s}".formatted(fileFilterDto));

        var validationResult = fileFilterDtoValidator.isValid(fileFilterDto);
        if (validationResult.hasErrors()) {
            throw new ValidationException(validationResult.getErrors());
        }
        List<FileInfo> fileInfoList = null;
        try (var connection = ConnectionHelper.createConnection()) {
            fileInfoList = fileInfoDao.findAll(connection, fileFilterDto);
        } catch (SQLException | DaoException e) {
            ConnectionHelper.closePool();
            throw new ServiceException(Message.SEARCH_ERROR.message, e);
        }
        log.debug("All the files:{%s} are found".formatted(fileInfoList));

        return fileInfoList.stream()
                .map(readFileInfoDtoMapper::map)
                .collect(Collectors.toList());
    }

    /**
     * Returns stream of the file data from file-storage for downloading it by a specific account.
     *
     * @param folder   an account folder.
     * @param fileName the name of specific file.
     * @return file data as a stream.
     */

    @SneakyThrows
    public Optional<InputStream> getFile(String folder, String fileName) {
        var fullPath = Path.of(BASE_DIR, folder, fileName);
        log.debug("Get file from the full path:{%s}".formatted(fullPath));

        return Files.exists(fullPath) ?
                Optional.of(Files.newInputStream(fullPath))
                : Optional.empty();
    }

    /**
     * Create all the necessary directories by transmitted folder name if they aren't exist.
     *
     * @param folder an account folder
     */

    @SneakyThrows
    public void createFolder(String folder) {
        Files.createDirectories(Path.of(BASE_DIR, folder));
    }

    /**
     * Returns output dto {@link ReadFileInfoDto readFileInfoDto} after uploading a specific file to the file-storage
     * by transmitted input dto {@link WriteFileInfoDto writeFileInfoDto}.
     * What about other file information,it is saved into the database using {@link FileInfoDao fileInfoDao}.
     * The saving process occurs after the mapping process{@link WriteFileInfoDtoMapper writeFileInfoDtoMapper}.
     *
     * @param writeFileInfoDto an input dto about file.
     * @return an output dto about a specific file.
     */

    public ReadFileInfoDto uploadFile(WriteFileInfoDto writeFileInfoDto) {
        log.debug("Upload file by writeFileInfoDto:{%s}".formatted(writeFileInfoDto));
        writeToStorage(writeFileInfoDto);
        var fileInfo = writeFileInfoDtoMapper.map(writeFileInfoDto);

        FileInfo savedFileInfo = null;
        try (var connection = ConnectionHelper.createConnection()) {
            savedFileInfo = fileInfoDao.save(connection, fileInfo);
        } catch (SQLException | DaoException e) {
            ConnectionHelper.closePool();
            throw new ServiceException(Message.SAVING_ERROR.message, e);
        }

        log.debug("The fileInfo:{%s} is saved".formatted(savedFileInfo));
        return readFileInfoDtoMapper.map(savedFileInfo);
    }

    /**
     * Returns the boolean result of the deleting file process by input dto about file{@link DeleteFileInfoDto deleteFileInfoDto}.
     *
     * @param deleteFileInfoDto an input dto about a specific file.
     * @return 'true' if a specific file has been deleted successfully else -'false'.
     */

    public boolean deleteFile(DeleteFileInfoDto deleteFileInfoDto) {
        log.debug("Delete file and fileInfo by deleteFileInfoDto:{%s}".formatted(deleteFileInfoDto));
        var fullPath = Path.of(BASE_DIR, deleteFileInfoDto.getFolder(), deleteFileInfoDto.getName());
        var id = Long.valueOf(deleteFileInfoDto.getId());

        try (var connection = ConnectionHelper.createConnection()) {
            if (fileInfoDao.delete(connection, id)) {
                Files.deleteIfExists(fullPath);
                log.debug("The file and fileInfo are deleted");
            }
        } catch (SQLException | IOException | DaoException e) {
            ConnectionHelper.closePool();
            throw new ServiceException(Message.DELETING_ERROR.message, e);
        }

        return Files.notExists(fullPath);
    }

    @SneakyThrows
    private void writeToStorage(WriteFileInfoDto writeFileInfoDto) {
        log.debug("Write file to storage by writeFileInfoDto:{%s}".formatted(writeFileInfoDto));
        var folder = writeFileInfoDto.getAccount().getFolder();
        var part = writeFileInfoDto.getPart();
        var fullPath = Path.of(BASE_DIR, folder, part.getSubmittedFileName());

        Files.createDirectories(fullPath.getParent());
        ByteBuffer byteBuffer = ByteBuffer.allocate(BUFFER_SIZE);
        try (ReadableByteChannel readableByteChannel = Channels.newChannel(part.getInputStream());
             WritableByteChannel writableByteChannel = Channels.newChannel(Files.newOutputStream(
                     fullPath,
                     StandardOpenOption.WRITE,
                     StandardOpenOption.CREATE,
                     StandardOpenOption.TRUNCATE_EXISTING))) {
            while (readableByteChannel.read(byteBuffer) > 0) {
                log.trace("The byte buffer:{%s} is filled".formatted(Arrays.toString(byteBuffer.array())));
                byteBuffer.flip();
                writableByteChannel.write(byteBuffer);
                byteBuffer.compact();
            }
        }
    }
}