package com.api.piotr.dsl;

import static com.api.piotr.util.GetApply.getApply;

import java.lang.reflect.InvocationTargetException;

public interface IdReference {

    public void setId(Long id);

    public static <T extends IdReference> T createIdRef(Class<T> entity, Long id) {
        try {
            return getApply(entity.getDeclaredConstructor().newInstance(), e -> e.setId(id));
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException
                | NoSuchMethodException ex) {
            String errorMsg = String.format("Failed to create a reference for entity %s with id %d: %s",
                    entity.getName(), id, ex.getMessage());
            throw new RuntimeException(errorMsg, ex);
        }
    }
}
