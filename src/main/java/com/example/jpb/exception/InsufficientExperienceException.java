package com.example.jpb.exception;

public class InsufficientExperienceException extends RuntimeException {

    public InsufficientExperienceException() {
        super("Candidate does not meet the required experience for this job");
    }

    public InsufficientExperienceException(Integer required, Integer actual) {
        super(String.format("Insufficient experience. Required: %d months, Candidate has: %d months", required, actual));
    }

    public InsufficientExperienceException(String message) {
        super(message);
    }

    public InsufficientExperienceException(String message, Throwable cause) {
        super(message, cause);
    }
}
