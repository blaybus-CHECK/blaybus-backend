package com.blaybus.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
class ProjectMatching {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long dataKey; // 데이터 키, 엔티티의 고유 식별자

    @ManyToOne(fetch = FetchType.LAZY) // 지연 로딩
    @JoinColumn(name = "project_id") // 외래 키 컬럼명 지정
    private Project project; // Project 엔티티와의 관계 설정

    @ManyToOne(fetch = FetchType.LAZY) // 지연 로딩
    @JoinColumn(name = "member_id") // 외래 키 컬럼명 지정
    private Member member; // Creation 엔티티와의 관계 설정

    private String matchingStatus; // 매칭 상태 (예: "PENDING", "ACCEPTED", "REJECTED" 등)
}
