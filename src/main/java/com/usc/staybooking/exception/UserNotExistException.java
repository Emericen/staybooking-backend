package com.usc.staybooking.exception;

public class UserNotExistException extends RuntimeException {
    public UserNotExistException(String message) {
        super(message);
    }
}