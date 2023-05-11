package com.example.vuelog.controller;

import com.example.vuelog.dto.request.LoginRequest;
import com.example.vuelog.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;

@Slf4j
@RestController @RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping(path = "/auth/login")
    public ResponseEntity<Void> login(@RequestBody @Valid LoginRequest loginRequest) {
        String accessToken = authService.signIn(loginRequest);
        ResponseCookie responseCookie = ResponseCookie.from("SESSION", accessToken)
                .domain("localhost")                                 // TODO 서버 환경에 따른 분리가 필요
                .path("/")
                .httpOnly(true)
                .secure(false)
                .maxAge(Duration.ofDays(30))
                .sameSite("Strict")
                .build();
        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                .build();
    }
}
