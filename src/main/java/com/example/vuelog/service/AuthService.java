package com.example.vuelog.service;

import com.example.vuelog.domain.Session;
import com.example.vuelog.domain.User;
import com.example.vuelog.dto.request.LoginRequest;
import com.example.vuelog.dto.response.SessionResponse;
import com.example.vuelog.exception.WrongSignInformation;
import com.example.vuelog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service @RequiredArgsConstructor @Transactional(readOnly = true)
public class AuthService {

    private final UserRepository userRepository;

    @Transactional
    public SessionResponse signIn(LoginRequest loginRequest) {
        User user = userRepository
                .findByEmailAndPassword(loginRequest.email(), loginRequest.password())
                .orElseThrow(WrongSignInformation::new);
        return SessionResponse.of(user.addSession().getAccessToken());
    }

}
