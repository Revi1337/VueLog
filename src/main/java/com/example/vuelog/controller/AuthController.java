package com.example.vuelog.controller;

import com.example.vuelog.config.AppConfig;
import com.example.vuelog.dto.request.LoginRequest;
import com.example.vuelog.dto.response.SessionResponse;
import com.example.vuelog.service.AuthService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;

@Slf4j
@RestController @RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    private final AppConfig appConfig;

    @PostMapping(path = "/auth/login")
    public SessionResponse login(@RequestBody @Valid LoginRequest loginRequest) {
        Long userId = authService.signIn(loginRequest);

        SecretKey secretKey = Keys.hmacShaKeyFor(Base64.getDecoder().decode(appConfig.jwtKey));
        String jws = Jwts.builder()
                .setSubject(String.valueOf(userId))
                .signWith(secretKey)
                .setIssuedAt(new Date())
                .compact();
        return SessionResponse.of(jws);
//
    }
}



//@Slf4j
//@RestController @RequiredArgsConstructor
//public class AuthController {
//
//    private final AuthService authService;
//
//    @PostMapping(path = "/auth/login")
//    public ResponseEntity<Void> login(@RequestBody @Valid LoginRequest loginRequest) {
//        String accessToken = authService.signIn(loginRequest);
//        ResponseCookie responseCookie = ResponseCookie.from("SESSION", accessToken)
//                .domain("localhost")                                 // TODO 서버 환경에 따른 분리가 필요
//                .path("/")
//                .httpOnly(true)
//                .secure(false)
//                .maxAge(Duration.ofDays(30))
//                .sameSite("Strict")
//                .build();
//        return ResponseEntity
//                .status(HttpStatus.OK)
//                .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
//                .build();
//    }
//}
