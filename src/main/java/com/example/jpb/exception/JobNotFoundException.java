package com.example.jpb.exception;

public class JobNotFoundException extends RuntimeException {

    public JobNotFoundException() {
        super("Job not found");
    }

    public JobNotFoundException(Long jobId) {
        super("Job not found with id: " + jobId);
    }

    public JobNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
