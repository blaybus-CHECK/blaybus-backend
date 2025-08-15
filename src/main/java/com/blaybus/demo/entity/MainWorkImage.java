package com.blaybus.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class MainWorkImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long dataKey; // 데이터 키, 엔티티의 고유 식별자

    private String uri; // 이미지 URI

    @ManyToOne(fetch = FetchType.LAZY) // 지연 로딩
    @JoinColumn(name = "main_work_id") // 외래 키 컬럼명 지정
    private MainWork mainWork;
}
