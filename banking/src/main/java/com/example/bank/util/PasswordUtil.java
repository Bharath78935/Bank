package com.example.bank.util;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtil {

    public static String hash(String input) {
        return BCrypt.hashpw(input, BCrypt.gensalt(12));
    }

    public static boolean verify(String plainText, String hashed) {
        return BCrypt.checkpw(plainText, hashed);
    }
}
