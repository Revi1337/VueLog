package com.example.vuelog.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = "specify email") @Email(message = "must be format email") String email,
        @NotBlank(message = "specify password") String password
) {
}
