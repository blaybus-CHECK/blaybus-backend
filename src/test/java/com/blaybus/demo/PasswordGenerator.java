package com.blaybus.demo;

import java.util.UUID;

public class PasswordGenerator {

    public static String generatePassword() {
        return "password" + UUID.randomUUID();
    }
}
