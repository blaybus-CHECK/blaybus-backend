package com.blaybus.demo;

import com.blaybus.demo.command.RegisterMainWorkCommand;

public class MainWorkPropertyValidator {
    public static boolean isDescriptionValid(RegisterMainWorkCommand command) {
        return command.description() != null && !command.description().isEmpty() && command.description().length() <= 1000;
    }

    public static boolean isTitleValid(RegisterMainWorkCommand command) {
        return command.title() != null && !command.title().isEmpty();
    }
}
