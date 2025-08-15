package com.blaybus.demo.entity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findById(UUID id);

    Optional<Member> findByEmail(String email);
}
