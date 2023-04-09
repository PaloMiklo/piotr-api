package com.api.piotr.error;

import com.api.piotr.enums.ErrorMessage;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String entityName, String id) {
        super(String.format("%s with id %s %s!", entityName, id, ErrorMessage.NOT_FOUND.getErrorMessage()));
    }

    public ResourceNotFoundException(String entityName, String dependedOnEntityName, String dependedOnEntityId,
            String id) {
        super(String.format("%s with id %s %s with id %s %s!", entityName, id, dependedOnEntityName, dependedOnEntityId,
                ErrorMessage.NOT_FOUND.getErrorMessage()));
    }
}
