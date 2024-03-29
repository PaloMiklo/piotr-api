package com.api.piotr.util;

import static com.api.piotr.constant.PaidOptions.PAYMENT;
import static com.api.piotr.constant.PaidOptions.TWO_DAY_SHIPPING_SHIPPING;
import static com.api.piotr.util.Utils.rethrow;
import static java.lang.Math.floor;
import static java.lang.Math.random;
import static java.util.Arrays.stream;
import static java.util.UUID.randomUUID;
import static java.util.stream.Collectors.joining;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.RecordComponent;
import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;
import java.util.Random;

import com.api.piotr.entity.PaidOption;
import com.api.piotr.entity.PaidOptionItem;

public class ObjectRandomizer {
    private static Random random = new Random();

    public static <T> T generateRandomObject(Class<T> clazz) throws InstantiationException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException,
            NoSuchFieldException {

        if (clazz.isRecord()) {
            Constructor<T> recordCnstrctr = clazz.getDeclaredConstructor(
                    stream(clazz.getRecordComponents())
                            .map(recordComponent -> recordComponent.getType()).toArray(Class<?>[]::new));

            Object[] recordCnstrctrArgs = stream(clazz.getRecordComponents())
                    .map(recordComponent -> randomizeRecordFields(recordComponent)).toArray(Object[]::new);

            return recordCnstrctr.newInstance(recordCnstrctrArgs);
        } else {
            return randomizeObjectFields(clazz.getDeclaredConstructor().newInstance());
        }
    }

    public static String generateRandomString(Integer length) {
        var chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";
        var builder = new StringBuilder();
        builder.append(random.ints(length, 0, chars.length())
                .mapToObj(i -> String.valueOf(chars.charAt(i)))
                .collect(joining()));

        // add a hyphen followed by a random character at the end
        builder.append("-");
        builder.append(chars.charAt(random.nextInt(chars.length())));

        return builder.toString();
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
                switch (fieldType.getSimpleName()) {
                    case "String":
                        field.set(object, generateRandomString(10));
                        break;
                    case "Integer":
                    case "int":
                        field.set(object, random.nextInt() & Integer.MAX_VALUE);
                        break;
                    case "Long":
                    case "long":
                        field.set(object, random.nextLong());
                        break;
                    case "Double":
                    case "double":
                        field.set(object, random.nextDouble());
                        break;
                    case "Float":
                    case "float":
                        field.set(object, random.nextFloat());
                        break;
                    case "Boolean":
                    case "boolean":
                        field.set(object, random.nextBoolean());
                        break;
                    case "BigDecimal":
                        field.set(object, BigDecimal.valueOf(123));
                        break;
                    case "Date":
                        field.set(object, new Date(random.nextLong()));
                        break;
                    case "LocalDate":
                        field.set(object, LocalDate.now());
                        break;
                    case "LocalTime":
                        field.set(object, LocalTime.now());
                        break;
                    case "LocalDateTime":
                        field.set(object, LocalDateTime.now().minusDays(1));
                        break;
                    case "Instant":
                        field.set(object, ZonedDateTime.now().toInstant());
                        break;
                    case "UUID":
                        field.set(object, randomUUID());
                        break;
                    case "Optional":
                        field.set(object, Optional.empty());
                        break;
                    case "List":
                    case "ArrayList":
                        field.set(object, new ArrayList<>());
                        break;
                    case "Set":
                        field.set(object, new HashSet<>());
                        break;
                    case "Map":
                        field.set(object, new HashMap<>());
                        break;
                    case "PaidOption":
                        field.set(object, new PaidOption(PAYMENT, PAYMENT.toLowerCase()));
                        break;
                    case "PaidOptionItem":
                        field.set(object, PaidOptionItemWrite.createInstance(TWO_DAY_SHIPPING_SHIPPING));
                        break;
                    case "Byte":
                    case "byte[]":
                        byte[] bytes = new byte[10];
                        random.nextBytes(bytes);
                        field.set(object, bytes);
                        break;
                    default:
                        Object nestedObject = fieldType.getDeclaredConstructor().newInstance();
                        var val = randomizeObjectFields(nestedObject);
                        field.set(object, val);
                }
            }
            return (T) object;
        });
    };

    private static Object randomizeRecordFields(RecordComponent component) {
        return Utils.rethrow("Failed to randomize record field!", () -> {
            Class<?> type = component.getType();
            Object value = null;
            switch (type.getSimpleName()) {
                case "String":
                    value = generateRandomString(10);
                    break;
                case "Integer":
                case "int":
                    value = random.nextInt() & Integer.MAX_VALUE;
                    ;
                    break;
                case "Long":
                case "long":
                    value = random.nextLong();
                    break;
                case "Double":
                case "double":
                    value = random.nextDouble();
                    break;
                case "Float":
                case "float":
                    value = random.nextFloat();
                    break;
                case "Boolean":
                case "boolean":
                    value = random.nextBoolean();
                    break;
                case "BigDecimal":
                    value = BigDecimal.valueOf(123);
                    break;
                case "Date":
                    value = new Date(random.nextLong());
                    break;
                case "LocalDate":
                    value = LocalDate.now();
                    break;
                case "LocalTime":
                    value = LocalTime.now();
                    break;
                case "LocalDateTime":
                    value = LocalDateTime.now();
                    break;
                case "Instant":
                    value = ZonedDateTime.now().toInstant();
                    break;
                case "UUID":
                    value = randomUUID();
                    break;
                case "Optional":
                    value = Optional.empty();
                    break;
                case "List":
                case "ArrayList":
                    value = new ArrayList<>();
                    break;
                case "Set":
                    value = new HashSet<>();
                    break;
                case "Map":
                    value = new HashMap<>();
                    break;
                case "Byte":
                case "byte[]":
                    byte[] bytes = new byte[10];
                    value = bytes;
                    break;
                default:
                    if (type == PaidOption.class) {
                        value = new PaidOption(PAYMENT, PAYMENT.toLowerCase());
                    } else if (type == PaidOptionItem.class) {
                        value = PaidOptionItemWrite.createInstance(TWO_DAY_SHIPPING_SHIPPING);
                    } else if (type.isEnum()) {
                        var constants = type.getEnumConstants();
                        value = floor(random() * constants.length);
                    } else {
                        try {
                            Class<?> nestedRecord = type;
                            Constructor<?> nestedRecordCnstrctr = nestedRecord.getDeclaredConstructor(
                                    stream(nestedRecord.getRecordComponents())
                                            .map(recordComponent -> recordComponent.getType())
                                            .toArray(Class<?>[]::new));

                            Object[] nestedRecordCnstrctrArgs = stream(nestedRecord.getRecordComponents())
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
            }
            return value;
        });
    }
}
