package com.test.entrance.exception;

public class ValidationException  extends RuntimeException {
    public ValidationException(String code) {
        super(code);
    }
}
