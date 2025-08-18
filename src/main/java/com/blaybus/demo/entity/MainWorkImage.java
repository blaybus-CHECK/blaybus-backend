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
}
