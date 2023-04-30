package com.example.vuelog.dto.request;

import com.example.vuelog.domain.Post;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record PostCreate(
        @NotBlank(message = "specify title") String title,
        @NotBlank(message = "specify content") String content
) {

    public Post toEntity() {
        return Post.builder()
                .title(title)
                .content(content)
                .build();
    }

}
