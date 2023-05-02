package com.example.vuelog.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record PostEdit(
        @NotBlank(message = "specify title") String title,
        @NotBlank(message = "specify content") String content
) {
}