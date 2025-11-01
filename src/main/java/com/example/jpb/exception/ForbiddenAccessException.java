package com.example.jpb.exception;

public class ForbiddenAccessException extends RuntimeException {

    public ForbiddenAccessException() {
        super("You do not have permission to access this resource");
    }

    public ForbiddenAccessException(String message) {
        super(message);
    }

    public ForbiddenAccessException(String message, Throwable cause) {
        super(message, cause);
    }
}
