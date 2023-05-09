package com.example.vuelog.controller;

import com.example.vuelog.dto.request.LoginRequest;
import com.example.vuelog.dto.response.SessionResponse;
import com.example.vuelog.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController @RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping(path = "/auth/login")
    public SessionResponse login(@RequestBody @Valid LoginRequest loginRequest) {
        return authService.signIn(loginRequest);
    }
}
