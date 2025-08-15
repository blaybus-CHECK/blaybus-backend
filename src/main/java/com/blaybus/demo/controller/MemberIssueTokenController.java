package com.blaybus.demo.controller;

import com.blaybus.demo.command.IssueMemberToken;
import com.blaybus.demo.entity.Member;
import com.blaybus.demo.entity.MemberRepository;
import com.blaybus.demo.result.AccessTokenCarrier;
import io.jsonwebtoken.Jwts;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public record MemberIssueTokenController(
    MemberRepository repository,
    PasswordEncoder passwordEncoder,
    JwtKeyHolder jwtKeyHolder
){

    @PostMapping("/api/member/issueToken")
    ResponseEntity<?> issueToken(@RequestBody IssueMemberToken query) {
        return repository
            .findByEmail(query.email())
            .filter(seller -> passwordEncoder.matches(
                query.password(),
                seller.getHashedPassword()
            ))
            .map(this::composeToken)
            .map(AccessTokenCarrier::new)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    private String composeToken(Member member) {
        return Jwts
            .builder()
            .claim("scp", "seller")
            .setSubject(member.getId().toString())
            .signWith(jwtKeyHolder.key())
            .compact();
    }
}
