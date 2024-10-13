package com.alex.web.data.storage.mapper;

import com.alex.web.data.storage.dto.ReadFileInfoDto;
import com.alex.web.data.storage.entity.FileInfo;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import lombok.extern.log4j.Log4j;

/**
 * This class describes a specific implementation of {@link Mapper mapper interface}.You can use it for mapping
 * from {@link FileInfo fileInfo} to {@link ReadFileInfoDto readFileInfoDto}.
 */

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Log4j
public final class ReadFileInfoDtoMapper implements Mapper<FileInfo, ReadFileInfoDto> {

    private static final String FILE_DESC = "DESC: The date of creation:[%1$s]; The size of file:[%2$d] bytes";
    private final static ReadFileInfoDtoMapper INSTANCE = new ReadFileInfoDtoMapper();

    public static ReadFileInfoDtoMapper getInstance() {
        return INSTANCE;
    }

    @Override
    public ReadFileInfoDto map(FileInfo fileInfo) {
        log.debug("Map the FileInfo:{%s} to ReadFileInfoDto".formatted(fileInfo));
        return ReadFileInfoDto.builder()
                .id(fileInfo.getId())
                .name(fileInfo.getName())
                .desc(FILE_DESC.formatted(fileInfo.getUploadDate(), fileInfo.getSize()))
                .accountId(fileInfo.getAccount().getId())
                .build();
    }
}
