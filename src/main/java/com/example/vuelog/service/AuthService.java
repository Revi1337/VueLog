package com.example.vuelog.service;

import com.example.vuelog.domain.User;
import com.example.vuelog.dto.request.LoginRequest;
import com.example.vuelog.exception.WrongSignInformation;
import com.example.vuelog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service @RequiredArgsConstructor @Transactional(readOnly = true)
public class AuthService {

    private final UserRepository userRepository;

    @Transactional
    public Long signIn(LoginRequest loginRequest) {
        return userRepository
                .findByEmailAndPassword(loginRequest.email(), loginRequest.password())
                .orElseThrow(WrongSignInformation::new)
                .getId();
    }

}
