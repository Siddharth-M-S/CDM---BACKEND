package com.admin.exception;

public class FailedToFetchOrderException extends RuntimeException {
    public FailedToFetchOrderException(String message) {
        super(message);
    }
}
