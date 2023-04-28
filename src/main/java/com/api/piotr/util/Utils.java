package com.api.piotr.util;

import static lombok.Lombok.sneakyThrow;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.util.function.ThrowingSupplier;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Utils {
    public static <T> T getApply(T entity, Consumer<T> method) {
        method.accept(entity);
        return entity;
    }

    public static <T> T rethrow(String message, ThrowingSupplier<T> f) {
        try {
            return f.get();
        } catch (VirtualMachineError | ThreadDeath | LinkageError e) {
            throw sneakyThrow(e);
        } catch (RuntimeException e) {
            throw new RuntimeException(message, e);
        } catch (Throwable e) {
            throw new RuntimeException(message, e);
        }
    }

    public static <T, U> List<U> mapToList(List<T> list, Function<T, U> mapper) {
        return list.stream()
                .map(mapper)
                .collect(Collectors.toList());
    }

}