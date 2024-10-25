package com.alex.web.data.storage.mapper;

import com.alex.web.data.storage.dto.WriteAccountDto;
import com.alex.web.data.storage.entity.Account;
import com.alex.web.data.storage.util.DateTimeFormatterHelper;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j;


import static lombok.AccessLevel.PRIVATE;

/**
 * This class describes a specific implementation of {@link Mapper mapper interface}.You can use it for mapping
 * from {@link WriteAccountDto writeAccountDto}
 * to {@link Account account}.
 */

//@Slf4j
@Log4j
@NoArgsConstructor(access = PRIVATE)
public final class WriteAccountDtoMapper implements Mapper<WriteAccountDto, Account>{

    private static final WriteAccountDtoMapper INSTANCE=new WriteAccountDtoMapper();

    public static WriteAccountDtoMapper getInstance(){
        return INSTANCE;
    }

    @Override
    public Account map(WriteAccountDto writeAccountDto) {
        log.debug("Map the WriteAccountDto:{%s} to Account".formatted(writeAccountDto));
        return Account.builder()
                .firstName(writeAccountDto.getFirstName())
                .lastName(writeAccountDto.getLastName())
                .birthDate(DateTimeFormatterHelper.parseDate(writeAccountDto.getBirthDate()))
                .login(writeAccountDto.getLogin())
                .password(writeAccountDto.getPassword())
                .folder(writeAccountDto.getFolder())
                .build();
    }
}