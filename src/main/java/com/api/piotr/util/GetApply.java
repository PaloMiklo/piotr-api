package com.api.piotr.util;

import java.util.function.Consumer;

public class GetApply {
    public static <T> T getApply(T entity, Consumer<T> method) {
        method.accept(entity);
        return entity;
    }
}
