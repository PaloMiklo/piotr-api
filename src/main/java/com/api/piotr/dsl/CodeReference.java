package com.api.piotr.dsl;

import static com.api.piotr.util.Utils.getApply;
import static com.api.piotr.util.Utils.rethrow;

public interface CodeReference {

    void setCode(String code);

    public static <T extends CodeReference> T createCodeRef(Class<T> entity, String code) {
        return rethrow(
                String.format("Failed to create a reference for entity %s with code %s!", entity.getName(), code),
                true,
                () -> getApply(entity.getDeclaredConstructor().newInstance(), e -> e.setCode(code)));
    }
}
