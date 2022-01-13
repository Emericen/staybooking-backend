package com.usc.staybooking.exception;

public class StayNotFoundException extends RuntimeException{
    public StayNotFoundException(String message) {
        super(message);
    }
}
