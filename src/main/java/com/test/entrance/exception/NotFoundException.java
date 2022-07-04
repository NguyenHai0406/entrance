package com.test.entrance.exception;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String code) {
        super(code);
    }
}
