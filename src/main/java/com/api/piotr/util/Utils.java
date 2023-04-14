package com.api.piotr.util;

import static lombok.Lombok.sneakyThrow;

import java.util.function.Consumer;

import org.springframework.util.function.ThrowingSupplier;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Utils {
    public static <T> T getApply(T entity, Consumer<T> method) {
        method.accept(entity);
        return entity;
    }

    public static <T> T rethrow(String message, boolean wrapRuntimeToo, ThrowingSupplier<T> f) {
        try {
            return f.get();
        } catch (VirtualMachineError | ThreadDeath | LinkageError e) {
            throw sneakyThrow(e);
        } catch (RuntimeException e) {
            if (wrapRuntimeToo)
                throw new RuntimeException(message, e);
            else
                throw e;
        } catch (Throwable e) {
            throw new RuntimeException(message, e);
        }
    }
}