package com.example.vuelog.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

@Entity @Getter @Table(name = "SESSION") @ToString
public class Session {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String accessToken;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;

    protected Session() {}

    @Builder
    public Session(User user) {
        this.accessToken = UUID.randomUUID().toString();
        this.user = user;
    }
}
