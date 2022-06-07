package com.techniners.EmailApp.exceptions;

public class UserNotFoundException extends EmailAppException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
