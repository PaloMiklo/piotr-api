package com.api.piotr;

import static com.api.piotr.util.Utils.rethrow;
import static java.lang.Math.floor;
import static java.lang.Math.random;
import static java.util.UUID.randomUUID;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.RecordComponent;
import java.math.BigDecimal;
import java.sql.Date;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import com.api.piotr.entity.PayedOption;
import com.api.piotr.entity.PayedOptionItem;
import com.api.piotr.util.PayedOptionItemWrite;
import com.api.piotr.util.Utils;

public class ObjectRandomizer {
    private static Random random = new Random();

    public static <T> T getRandomObject(Class<T> clazz) throws InstantiationException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException,
            NoSuchFieldException {

        if (clazz.isRecord()) {
            Constructor<T> recordCnstrctr = clazz.getDeclaredConstructor(
                    Arrays.stream(clazz.getRecordComponents())
                            .map(recordComponent -> recordComponent.getType()).toArray(Class<?>[]::new));

            Object[] recordCnstrctrArgs = Arrays.stream(clazz.getRecordComponents())
                    .map(recordComponent -> randomizeRecordFields(recordComponent)).toArray(Object[]::new);

            return recordCnstrctr.newInstance(recordCnstrctrArgs);
        } else {
            return randomizeObjectFields(clazz.getDeclaredConstructor().newInstance());
        }
    }

    @SuppressWarnings("unchecked")
    private static <T> T randomizeObjectFields(T objArg) {
        return rethrow("Failed to randomize object field!", () -> {
            Class<?> clazz = objArg.getClass();
            Field[] fields = clazz.getDeclaredFields();
            var object = clazz.getDeclaredConstructor().newInstance();

            for (Field field : fields) {
                Class<?> fieldType = field.getType();
                field.setAccessible(true);

                if (fieldType.equals(String.class)) {
                    field.set(object, generateRandomString(10));
                } else if (fieldType.equals(Integer.class) || fieldType.equals(int.class)) {
                    field.set(object, random.nextInt());
                } else if (fieldType.equals(Long.class) || fieldType.equals(long.class)) {
                    field.set(object, random.nextLong());
                } else if (fieldType.equals(Double.class) || fieldType.equals(double.class)) {
                    field.set(object, random.nextDouble());
                } else if (fieldType.equals(Float.class) || fieldType.equals(float.class)) {
                    field.set(object, random.nextFloat());
                } else if (fieldType.equals(Boolean.class) ||
                        fieldType.equals(boolean.class)) {
                    field.set(object, random.nextBoolean());
                } else if (fieldType.equals(BigDecimal.class)) {
                    field.set(object, BigDecimal.valueOf(random.nextLong()));
                } else if (fieldType.equals(Date.class)) {
                    field.set(object, new Date(random.nextLong()));
                } else if (fieldType.isArray()) {
                    var elementType = fieldType.getComponentType();
                    if (elementType.equals(Byte.class))
                        field.set(object, (random() * Byte.MAX_VALUE));
                } else if (fieldType.equals(LocalDate.class)) {
                    field.set(object, LocalDate.now());
                } else if (fieldType.equals(LocalTime.class)) {
                    field.set(object, LocalTime.now());
                } else if (fieldType.equals(LocalDateTime.class)) {
                    field.set(object, LocalDateTime.now());
                } else if (fieldType.equals(Instant.class)) {
                    field.set(object, ZonedDateTime.now().toInstant());
                } else if (fieldType.equals(UUID.class)) {
                    field.set(object, randomUUID());
                } else if (fieldType.equals(Optional.class)) {
                    field.set(object, Optional.empty());
                } else if (fieldType.equals(List.class) | fieldType.equals(ArrayList.class)) {
                    field.set(object, new ArrayList<>());
                } else if (fieldType.equals(Set.class)) {
                    field.set(object, new HashSet<>());
                } else if (fieldType.equals(Map.class)) {
                    field.set(object, new HashMap<>());
                } else if (fieldType.equals(PayedOption.class)) {
                    field.set(object, new PayedOption("PAYMENT", "payment"));
                } else if (fieldType.equals(PayedOptionItem.class)) {
                    field.set(object,
                            PayedOptionItemWrite.createInstance("2_DAY_SHIPPING-SHIPPING"));
                } else if (fieldType.isEnum()) {
                    var constants = fieldType.getEnumConstants();
                    field.set(object, floor(random() * constants.length));
                } else {
                    Object nestedObject = fieldType.getDeclaredConstructor().newInstance();
                    var val = randomizeObjectFields(nestedObject);
                    field.set(object, val);
                }
            }
            return (T) object;
        });
    }

    private static Object randomizeRecordFields(RecordComponent component) {
        return Utils.rethrow("Failed to randomize record field!", () -> {
            Class<?> type = component.getType();
            Object value = null;
            if (type == String.class) {
                value = generateRandomString(10);
            } else if (type == Integer.class || type == int.class) {
                value = random.nextInt();
            } else if (type == Long.class || type == long.class) {
                value = random.nextLong();
            } else if (type == Double.class || type == double.class) {
                value = random.nextDouble();
            } else if (type == Float.class || type == float.class) {
                value = random.nextFloat();
            } else if (type == Boolean.class || type == boolean.class) {
                value = random.nextBoolean();
            } else if (type == BigDecimal.class) {
                value = BigDecimal.valueOf(random.nextLong());
            } else if (type == Date.class) {
                value = new Date(random.nextLong());
            } else if (type == String.class) {
                var elementType = type.getComponentType();
                if (elementType.equals(Byte.class))
                    value = (random() * Byte.MAX_VALUE);
            } else if (type == LocalDate.class) {
                value = LocalDate.now();
            } else if (type == LocalTime.class) {
                value = LocalTime.now();
            } else if (type == LocalDateTime.class) {
                value = LocalDateTime.now();
            } else if (type == Instant.class) {
                value = ZonedDateTime.now().toInstant();
            } else if (type == UUID.class) {
                value = randomUUID();
            } else if (type == Optional.class) {
                value = Optional.empty();
            } else if (type == List.class | type.equals(ArrayList.class)) {
                value = new ArrayList<>();
            } else if (type == Set.class) {
                value = new HashSet<>();
            } else if (type == Map.class) {
                value = new HashMap<>();
            } else if (type == PayedOption.class) {
                value = new PayedOption("PAYMENT", "payment");
            } else if (type == PayedOptionItem.class) {
                value = PayedOptionItemWrite.createInstance("2_DAY_SHIPPING-SHIPPING");
            } else if (type.isEnum()) {
                var constants = type.getEnumConstants();
                value = floor(random() * constants.length);
            } else {
                try {
                    Class<?> nestedRecord = type;
                    Constructor<?> nestedRecordCnstrctr = nestedRecord.getDeclaredConstructor(
                            Arrays.stream(nestedRecord.getRecordComponents())
                                    .map(recordComponent -> recordComponent.getType()).toArray(Class<?>[]::new));

                    Object[] nestedRecordCnstrctrArgs = Arrays.stream(nestedRecord.getRecordComponents())
                            .map(recordComponent -> randomizeRecordFields(recordComponent))
                            .toArray(Object[]::new);

                    Object recordInstance = nestedRecordCnstrctr.newInstance(nestedRecordCnstrctrArgs);

                    value = recordInstance;
                } catch (NoSuchMethodException e) {
                    throw new RuntimeException(
                            "The nested object doesn't have a no-argument constructor, so it is not possible to create a new instance! "
                                    + e.getMessage());
                }
            }
            return value;
        });
    };

    private static String generateRandomString(Integer length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";
        return new StringBuilder()
                .append(random.ints(length, 0, chars.length())
                        .mapToObj(i -> chars.charAt(i))
                        .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append))
                .toString();
    }
}
