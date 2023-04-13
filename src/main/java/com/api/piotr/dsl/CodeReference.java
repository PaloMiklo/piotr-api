package com.api.piotr.dsl;

import static com.api.piotr.util.GetApply.getApply;

import java.lang.reflect.InvocationTargetException;

public interface CodeReference {

    void setCode(String code);

    public static <T extends CodeReference> T createCodeRef(Class<T> entity, String code) {
        try {
            return getApply(entity.getDeclaredConstructor().newInstance(), e -> e.setCode(code));
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException
                | NoSuchMethodException ex) {
            String errorMsg = String.format("Failed to create a reference for entity %s with code %s: %s",
                    entity.getName(), code, ex.getMessage());
            throw new RuntimeException(errorMsg, ex);
        }
    }
}
