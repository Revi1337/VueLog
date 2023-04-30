package com.example.vuelog.dto.response;

import com.example.vuelog.domain.Post;
import lombok.Builder;

@Builder
public record PostResponse(
        Long id,
        String title,
        String content
) {

    public static PostResponse from(Post post) {
        return PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle().substring(0, Math.min(post.getTitle().length(), 10)))
                .content(post.getContent())
                .build();
    }

}
