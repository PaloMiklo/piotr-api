package com.api.piotr.error;

import com.api.piotr.enums.ErrorMessage;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String target, String id) {
        super(String.format("%s with id %s %s!", target, id, ErrorMessage.NOT_FOUND.getErrorMessage()));
    }
}
