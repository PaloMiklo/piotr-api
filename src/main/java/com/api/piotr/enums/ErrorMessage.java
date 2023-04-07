package com.api.piotr.enums;

public enum ErrorMessage {
    NOT_FOUND("Not found");

    private final String errorMessage;

    ErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
