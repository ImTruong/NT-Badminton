package com.dev.NT_Badminton.util;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Component;

import java.text.Normalizer;

@Component
public class Utils {
    public static int PAGE_SIZE = 20;
    private static final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    public static String removeCharacterVn(String input) {
        if (input == null || input.isEmpty()) {
            return "";
        }

        // Chuẩn hóa Unicode và loại bỏ dấu
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        String output = normalized.replaceAll("\\p{M}", "");

        // Chuyển về chữ thường, thay thế khoảng trắng và ký tự không hợp lệ
        output = output.toLowerCase().replaceAll("[^a-z0-9\s]", "").replaceAll("\s+", "-");

        return output;
    }

    public static String randomString(int count, String characters) {
        return RandomStringUtils.random(count, characters);
    }

    public static String randomString(int count) {
        return randomString(count, ALPHA_NUMERIC_STRING);
    }
}
