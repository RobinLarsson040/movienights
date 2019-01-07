package com.robin.ws.webserviceexample.exceptions;

public class UserServiceException extends RuntimeException {

    public UserServiceException(String message)
    {
        super(message);
    }
}
