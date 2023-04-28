package com.api.piotr.util;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.lang.reflect.InvocationTargetException;
import java.sql.Date;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import lombok.Getter;
import lombok.Setter;

public class ObjectRandomizerTest {

    @Test
    public void testGetRandomObject() throws IllegalAccessException, InstantiationException,
            IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException,
            NoSuchFieldException {
        var myObject = ObjectRandomizer.generateRandomObject(TestClass.class);
        assertNotNull(myObject.getName());
        assertNotNull(myObject.getAge());
        assertNotNull(myObject.getOther());
        assertNotNull(myObject.getArrayOutter());
        assertNotNull(myObject.getOther().getOtherName());
        assertNotNull(myObject.getOther().getOtherAge());
        assertNotNull(myObject.getOther().getOtherDate());
        assertNotNull(myObject.getOther().getArrayInner());
    }

    @Getter
    @Setter
    static class TestClass {
        private String name;
        private Integer age;
        private OtherTestClass other;
        private ArrayList<Integer> arrayOutter = new ArrayList<>();
    }

    @Getter
    @Setter
    static class OtherTestClass {
        private String otherName;
        private Integer otherAge;
        private Date otherDate;
        private ArrayList<Integer> arrayInner = new ArrayList<>();
    }
}
