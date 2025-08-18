package com.blaybus.demo.entity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface MainWorkRepository extends JpaRepository<MainWork, Long> {
    Optional<MainWork> findById(UUID id); // UUID로 MainWork 찾기
}
