package com.example.vuelog.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.util.Assert;

@Entity @Table(name = "POST") @Getter @ToString
public class Post {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "POST_ID")
    private Long id;

    @Column(name = "TITLE" )
    private String title;

    @Lob @Column(name = "CONTENT")
    private String content;

    protected Post() {}

    @Builder
    private Post(String title, String content) {
        Assert.notNull(title, "title cannot be null");
        Assert.notNull(content, "content cannot be null");
        this.title = title;
        this.content = content;
    }

    public PostEditor.PostEditorBuilder toEditor() {
        return PostEditor.builder()
                .title(title)
                .content(content);
    }

    public void edit(PostEditor postEditor) {
        title = postEditor.getTitle();
        content = postEditor.getContent();
    }

}
