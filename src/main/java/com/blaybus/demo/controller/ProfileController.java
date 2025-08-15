package com.blaybus.demo.controller;

import com.blaybus.demo.entity.Profile;
import com.blaybus.demo.entity.ProfileRepository;
import com.blaybus.demo.command.RegisterProfileCommand;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public record ProfileController(ProfileRepository repository) {

    @PostMapping("/api/profile/register")
    public ResponseEntity<?> registerProfile(@RequestBody RegisterProfileCommand command) {
        Profile profile = new Profile();
        profile.setArtistIntro(command.artistIntro());
        profile.setArtistCareer(command.artistCareer());
        profile.setPortfolioUrl(command.portfolioUrl());
        profile.setContact(command.contact());
        profile.setEmail(command.email());
        profile.setSnsUrl(command.snsUrl());
        repository.save(profile);
        return ResponseEntity.noContent().build();
    }
}
