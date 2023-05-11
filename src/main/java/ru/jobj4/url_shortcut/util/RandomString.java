package ru.jobj4.url_shortcut.util;

import java.security.SecureRandom;
import java.util.Base64;

public class RandomString {

    public static String randomString() {
        int stringLength = 12;
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[stringLength];
        random.nextBytes(bytes);
        return Base64.getEncoder().encodeToString(bytes);
    }
}
