package com.blaybus.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long dataKey;

    private String profileImageUri;

    private String artistIntro;

    private int artistCareer;

    private String portfolioUrl;

    private String contact;

    private String email;

    private String snsUrl;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private MainWork mainwork;
}
