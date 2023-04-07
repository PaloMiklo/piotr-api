package com.api.piotr.error;

public class ProcessingException extends RuntimeException {
    public ProcessingException(String target, Throwable cause) {
        super(String.format("Error processing %s: %s", target, cause.getMessage()));
    }
}
