package com.example.jpb.exception;

public class EmailAlreadyExistsException extends RuntimeException {

    public EmailAlreadyExistsException(String email) {
        super("Email already registered: " + email);
    }

    public EmailAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
