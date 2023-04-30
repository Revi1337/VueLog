package com.example.vuelog.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.ToString;

@Entity @Table(name = "POST") @Getter @ToString
public class Post {

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "POST_ID")
    private Long id;

    @Column(name = "TITLE" )
    private String title;

    @Lob @Column(name = "CONTENT")
    private String content;

    protected Post() {}

    private Post(Long id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }

    public static Post of(String title, String content) {
        return new Post(null, title, content);
    }

}
