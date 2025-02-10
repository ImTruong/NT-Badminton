package com.dev.NT_Badminton.util;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Component;

@Component
public class Utils {

    private static final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    public static String randomString(int count, String characters) {
        return RandomStringUtils.random(count, characters);
    }

    public static String randomString(int count) {
        return randomString(count, ALPHA_NUMERIC_STRING);
    }
}
