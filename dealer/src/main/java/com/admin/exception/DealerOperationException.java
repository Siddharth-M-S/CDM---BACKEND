package com.admin.exception;

public class DealerOperationException extends RuntimeException {

    public DealerOperationException(String message) {
        super(message);
    }

    public DealerOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}
