package com.example.vuelog.dto.response;

public record SessionResponse(
        String accessToken
) {
    public static SessionResponse of(String accessToken) {
        return new SessionResponse(accessToken);
    }
}
