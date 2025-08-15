package com.blaybus.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
public class ProjectAttachment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long dataKey; // 데이터 키, 엔티티의 고유 식별자

    @Column(unique = true)
    private UUID id; // UUID로 고유 식별자 설정

    private String uri; // 첨부 파일 URI

    @ManyToOne(fetch = FetchType.LAZY) // 지연 로딩
    @JoinColumn(name = "project_id") // 외래 키 컬럼명 지정
    private Project project; // Project 엔티티와의 관계 설정
}
