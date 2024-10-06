package com.alex.web.data.storage.mapper;

import com.alex.web.data.storage.dto.ReadAccountDto;
import com.alex.web.data.storage.entity.Account;
import lombok.NoArgsConstructor;

import lombok.extern.log4j.Log4j;

import static lombok.AccessLevel.*;

/**
 * This class describes a specific implementation of {@link Mapper mapper interface}.You can use it for mapping
 * from {@link Account Account} to {@link ReadAccountDto readAccopuntDto}.
 */

@Log4j
@NoArgsConstructor(access = PRIVATE)
public final class ReadAccountDtoMapper implements Mapper<Account, ReadAccountDto> {
    private static final ReadAccountDtoMapper INSTANCE = new ReadAccountDtoMapper();

    public static ReadAccountDtoMapper getInstance() {
        return INSTANCE;
    }

    @Override
    public ReadAccountDto map(Account account) {
        log.debug("Map the Account:{%s} to ReadAccountDto".formatted(account));
        return ReadAccountDto.builder()
                .id(account.getId())
                .firstName(account.getFirstName())
                .lastName(account.getLastName())
                .birthDate(account.getBirthDate())
                .login(account.getLogin())
                .role(account.getRole())
                .folder(account.getFolder())
                .build();
    }
}