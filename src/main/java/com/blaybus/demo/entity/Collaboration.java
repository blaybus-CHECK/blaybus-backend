package com.blaybus.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Entity
@Setter
@Getter
public class Collaboration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long dataKey;

    @Column(unique = true)
    private UUID id;

    private String title;

    private String content;

    @OneToMany(mappedBy = "collaboration",
        fetch = FetchType.LAZY, // 지연 로딩
        cascade = CascadeType.ALL, // 영속성 전이
        orphanRemoval = true) // 관계 해제 시 DB에서도 삭제 )
    private List<CollaborationImage> images;
}
