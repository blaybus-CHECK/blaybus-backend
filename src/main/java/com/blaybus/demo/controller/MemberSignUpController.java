package com.blaybus.demo.controller;

import com.blaybus.demo.command.CreateMemberCommand;
import com.blaybus.demo.entity.Member;
import com.blaybus.demo.entity.MemberRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static com.blaybus.demo.UserPropertyValidator.*;

@RestController
public record MemberSignUpController(
    MemberRepository repository,
    PasswordEncoder passwordEncoder
) {

    @PostMapping("/api/member/signUp")
    ResponseEntity<?> signUp(@RequestBody CreateMemberCommand command) {
        if (isCommandValid(command) == false) {
            return ResponseEntity.badRequest().build();
        }

        UUID id = UUID.randomUUID();
        String hashedPassword = passwordEncoder.encode(command.password());
        var seller = new Member();
        seller.setId(id);
        seller.setEmail(command.email());
        seller.setHashedPassword(hashedPassword);
        repository.save(seller);

        return ResponseEntity.noContent().build();
    }

    private static boolean isCommandValid(CreateMemberCommand command) {
        return isEmailValid(command.email())
            && isPasswordValid(command.password());
    }
}
