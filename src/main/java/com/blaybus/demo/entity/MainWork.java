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

    private String title; // 작품 제목

    private String content; // 작품 내용

    @OneToMany(mappedBy = "mainWork",
        fetch = FetchType.LAZY, // 지연 로딩
        cascade = CascadeType.ALL, // 영속성 전이
        orphanRemoval = true) // 관계 해제 시 DB에서도 삭제
    private List<MainWorkImage> images; // 작품 이미지 목록
}
