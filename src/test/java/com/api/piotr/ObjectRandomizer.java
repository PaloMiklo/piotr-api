package com.api.piotr;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ObjectRandomizer {
    private static Random random = new Random();

    public static <T> T getRandomObject(Class<T> clazz) throws IllegalAccessException, InstantiationException,
            IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
        T object = clazz.getDeclaredConstructor().newInstance();
        randomizeObjectFields(object);
        return object;
    }

    private static void randomizeObjectFields(Object object) {
        Class<?> clazz = object.getClass();
        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);

            try {
                Object fieldValue = field.get(object);

                if (fieldValue == null) {
                    continue;
                }

                Class<?> fieldType = field.getType();

                if (fieldType.equals(String.class)) {
                    field.set(object, generateRandomString());
                } else if (fieldType.equals(Integer.class) || fieldType.equals(int.class)) {
                    field.set(object, random.nextInt());
                } else if (fieldType.equals(Long.class) || fieldType.equals(long.class)) {
                    field.set(object, random.nextLong());
                } else if (fieldType.equals(Double.class) || fieldType.equals(double.class)) {
                    field.set(object, random.nextDouble());
                } else if (fieldType.equals(Float.class) || fieldType.equals(float.class)) {
                    field.set(object, random.nextFloat());
                } else if (fieldType.equals(Boolean.class) || fieldType.equals(boolean.class)) {
                    field.set(object, random.nextBoolean());
                } else if (fieldType.equals(Date.class)) {
                    field.set(object, new Date(random.nextLong()));
                } else if (fieldType.equals(Class.class)) {
                    field.set(object, ObjectRandomizer.class);
                } else if (List.class.isAssignableFrom(fieldType)) {
                    List<Object> list = new ArrayList<>();
                    Class<?> listElementType = (Class<?>) ((java.lang.reflect.ParameterizedType) field.getGenericType())
                            .getActualTypeArguments()[0];
                    for (int i = 0; i < 3; i++) {
                        Object listItem = listElementType.getDeclaredConstructor().newInstance();
                        randomizeObjectFields(listItem);
                        list.add(listItem);
                    }
                    field.set(object, list);
                } else {
                    Object nestedObject = fieldType.getDeclaredConstructor().newInstance();
                    randomizeObjectFields(nestedObject);
                    field.set(object, nestedObject);
                }
            } catch (IllegalAccessException | InstantiationException | IllegalArgumentException
                    | InvocationTargetException | NoSuchMethodException | SecurityException e) {
                // handle exception
            }
        }
    }

    private static String generateRandomString() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < 10; i++) {
            int index = random.nextInt(chars.length());
            sb.append(chars.charAt(index));
        }

        return sb.toString();
    }

}
