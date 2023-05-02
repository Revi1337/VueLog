package com.example.vuelog.dto.request;

import com.example.vuelog.domain.Post;
import com.example.vuelog.exception.InvalidRequestException;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record PostCreate(
        @NotBlank(message = "specify title") String title,
        @NotBlank(message = "specify content") String content
) {

    public void validate() {
        if (title.contains("바보"))
            throw new InvalidRequestException("title", "cant include idiot in title");
    }

    public Post toEntity() {
        return Post.builder()
                .title(title)
                .content(content)
                .build();
    }

}
