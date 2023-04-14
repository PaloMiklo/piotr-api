package com.api.piotr.dsl;

import static com.api.piotr.util.Utils.getApply;
import static com.api.piotr.util.Utils.rethrow;

public interface IdReference {

    public void setId(Long id);

    public static <T extends IdReference> T createIdRef(Class<T> entity, Long id) {
        return rethrow(
                String.format("Failed to create a reference for entity %s with id %d!", entity.getName(), id),
                () -> getApply(entity.getDeclaredConstructor().newInstance(), e -> e.setId(id)));
    }
}
