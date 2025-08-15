package com.blaybus.demo.command;

public record RegisterProfileCommand(
    String artistIntro,
    int artistCareer,
    String portfolioUrl,
    String contact,
    String email,
    String snsUrl
) {

}
