package com.alex.web.data.storage.exception;

/**
 * This class describes exception which can happen in the DAO layer.
 */

public class DaoException extends RuntimeException {
    public DaoException(Throwable cause){
        super(cause);
    }
    public DaoException(String message){
        super(message);
    }
    public DaoException(String message, Throwable cause){
        super(message,cause);
    }
}