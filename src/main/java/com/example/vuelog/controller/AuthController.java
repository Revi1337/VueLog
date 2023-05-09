package com.example.vuelog.controller;

import com.example.vuelog.domain.User;
import com.example.vuelog.dto.request.LoginRequest;
import com.example.vuelog.exception.WrongSignInformation;
import com.example.vuelog.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController @RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;

    @PostMapping(path = "/auth/login")
    public User login(@RequestBody @Valid LoginRequest loginRequest) {
        log.info(">> {}", loginRequest);
        User user = userRepository.findByEmailAndPassword(loginRequest.email(), loginRequest.password())
                .orElseThrow(WrongSignInformation::new);
        return user;
    }
}
