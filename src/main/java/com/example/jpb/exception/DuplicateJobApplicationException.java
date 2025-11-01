package com.example.jpb.exception;

public class DuplicateJobApplicationException extends RuntimeException {

    public DuplicateJobApplicationException() {
        super("You have already applied to this job");
    }

    public DuplicateJobApplicationException(Long jobId, Long candidateId) {
        super("Candidate " + candidateId + " has already applied to job " + jobId);
    }

    public DuplicateJobApplicationException(String message) {
        super(message);
    }

    public DuplicateJobApplicationException(String message, Throwable cause) {
        super(message, cause);
    }
}
