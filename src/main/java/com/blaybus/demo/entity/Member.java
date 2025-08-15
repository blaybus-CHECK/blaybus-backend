package com.blaybus.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Setter
@Getter
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long dataKey;

    @Column(unique = true)
    private UUID id;

    @Column(unique = true)
    private String email;

    private String hashedPassword;

    @OneToOne(
        fetch = FetchType.LAZY, // 지연 로딩
        cascade = CascadeType.ALL, // 영속성 전이
        orphanRemoval = true // 관계 해제 시 DB에서도 삭제
    )
    @JoinColumn(name = "profile_id") // 외래 키 컬럼명 지정
    private Profile profile;

}
