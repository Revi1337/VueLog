package com.example.vuelog.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Getter @ToString
@Entity @Table(name = "USERS", uniqueConstraints = {
        @UniqueConstraint(name = "email", columnNames = "email")
})
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ID")
    private Long id;

    private String name;

    private String email;

    private String password;

    private LocalDateTime createdAt;

    @ToString.Exclude
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Session> sessions = new ArrayList<>();

    public Session addSession() {
        Session session = Session.builder().user(this).build();
        this.sessions.add(session);
        return session;
    }

    protected User() {}

    @Builder(builderMethodName = "create")
    private User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.createdAt = LocalDateTime.now();
    }

}
