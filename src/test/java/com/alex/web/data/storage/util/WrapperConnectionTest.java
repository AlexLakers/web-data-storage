package com.alex.web.data.storage.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.LinkedBlockingDeque;

@ExtendWith(MockitoExtension.class)
class WrapperConnectionTest {

    @Mock
    private Connection connection;
    @Mock
    private LinkedBlockingDeque<Connection> linkedBlockingDeque;
    @InjectMocks
    private WrapperConnection wrapperConnection;

    @Test
    void close_shouldAddWrapperConnectionToPool_whenMethodCloseIsCalled() throws SQLException {
        wrapperConnection.close();

        Mockito.verify(linkedBlockingDeque).add(Mockito.any(WrapperConnection.class));
    }
}