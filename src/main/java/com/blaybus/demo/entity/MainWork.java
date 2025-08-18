package com.blaybus.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
public class MainWork {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long dataKey; // 데이터 키, 엔티티의 고유 식별자

    @Column(unique = true)
    private UUID id;

    private UUID memberId; // 회원 ID

    private String title; // 메인 작업 제목

    private String description; // 메인 작업 설명

//    private String[] imageUris; // 이미지 URI 배열
}
