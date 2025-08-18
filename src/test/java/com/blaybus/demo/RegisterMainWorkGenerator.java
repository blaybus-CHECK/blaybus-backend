package com.blaybus.demo;

import com.blaybus.demo.command.RegisterMainWorkCommand;

import java.util.UUID;

public class RegisterMainWorkGenerator {
    public static RegisterMainWorkCommand generateRegisterMainWorkCommand() {
        return new RegisterMainWorkCommand(
            generateTitle(),
            generateDescription()
        );
    }

    public static RegisterMainWorkCommand generateRegisterMainWorkCommandWithTitle(String title) {
        return new RegisterMainWorkCommand(
            title,
            generateDescription()
        );
    }

    public static RegisterMainWorkCommand generateRegisterMainWorkCommandWithDescription(String description) {
        return new RegisterMainWorkCommand(
            generateTitle(),
            description
        );
    }

    public static RegisterMainWorkCommand generateRegisterMainWorkCommandWithImageUrls(String[] imageUrls) {
        return new RegisterMainWorkCommand(
            generateTitle(),
            generateDescription()
        );
    }

    private static String generateTitle() {
        return "title" + UUID.randomUUID();
    }

    private static String generateDescription() {
        return "description" + UUID.randomUUID();
    }
}
