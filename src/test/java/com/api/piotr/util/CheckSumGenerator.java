package com.api.piotr.util;

import java.util.Random;
import java.util.zip.CRC32;
import org.junit.jupiter.api.Test;

public class CheckSumGenerator {
    @Test
    void generate() {
        String input = generateRandomString(10);
        byte[] bytes = input.getBytes();
        CRC32 crc32 = new CRC32();
        crc32.update(bytes);
        long checksumValue = crc32.getValue();
        System.out.println(checksumValue);
    }

    private String generateRandomString(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder(length);
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }
}
