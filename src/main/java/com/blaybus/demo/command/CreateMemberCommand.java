package com.blaybus.demo.command;

public record CreateMemberCommand(
    String email,
    String password
) {
}
