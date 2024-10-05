package com.alex.web.data.storage.exception;

/**
 * This class describes exception which can happen in the Service layer.
 */

public class ServiceException extends RuntimeException {
    public ServiceException(String message){
        super(message);
    }
    public ServiceException(String message,Throwable cause){
        super(message,cause);
    }
    public ServiceException(Throwable cause){
        super(cause);
    }
}
