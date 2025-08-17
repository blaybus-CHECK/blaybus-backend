package com.blaybus.demo.controller;

import com.blaybus.demo.entity.Member;
import com.blaybus.demo.entity.MemberRepository;
import com.blaybus.demo.view.MemberMeView;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.UUID;

@RestController
public record MemberMeController(
    MemberRepository repository
) {

    @GetMapping("/api/member/me")
    MemberMeView memberMe(Principal user) {
        UUID id = UUID.fromString(user.getName());
        Member member = repository.findById(id).orElseThrow();
        return new MemberMeView(id, member.getEmail());
    }
}
