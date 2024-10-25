package com.alex.web.data.storage.mapper;

import com.alex.web.data.storage.dto.WriteFileInfoDto;
import com.alex.web.data.storage.entity.FileInfo;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import lombok.extern.log4j.Log4j;

/**
 * This class describes a specific implementation of {@link Mapper mapper interface}.You can use it for mapping
 * from {@link WriteFileInfoDto writeFileInfoDto}
 * to {@link FileInfo fileInfo}.
 */

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Log4j
public final class WriteFileInfoDtoMapper implements Mapper<WriteFileInfoDto, FileInfo>{
    private final static WriteFileInfoDtoMapper INSTANCE = new WriteFileInfoDtoMapper();
    public static WriteFileInfoDtoMapper getInstance() {
        return INSTANCE;
    }
    @Override
    public FileInfo map(WriteFileInfoDto writeFileInfoDto){
        log.debug("Map the WriteFileInfoDto:{%s} to FileInfo".formatted(writeFileInfoDto));
        return FileInfo.builder()
                .name(writeFileInfoDto.getPart().getSubmittedFileName())
                .uploadDate(writeFileInfoDto.getUploadDate())
                .size(writeFileInfoDto.getPart().getSize())
                .account(writeFileInfoDto.getAccount())
                .build();

    }
}