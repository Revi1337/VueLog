package com.example.vuelog.config.mvc;

import com.example.vuelog.domain.Session;
import com.example.vuelog.exception.UnAuthorizationException;
import com.example.vuelog.repository.SessionRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Base64;


@RequiredArgsConstructor @Slf4j
public class AuthResolver implements HandlerMethodArgumentResolver {

    private final SessionRepository sessionRepository;

    private static final String BEARER_PREFIX = "Bearer ";

    private static final String KEY = "JyctHB7tMimFbk+KD2yswRiyxGfO9XDrBi6bxaaD8vY=";

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(UserSession.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) throws Exception {
        String authHeaderValue = webRequest.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeaderValue == null || !authHeaderValue.startsWith(BEARER_PREFIX))
            throw new UnAuthorizationException();
        String jwtToken = authHeaderValue.substring(BEARER_PREFIX.length());
        byte[] decodedKey = Base64.getDecoder().decode(KEY);
        try {
            Jws<Claims> claimsJws = Jwts.parserBuilder()
                    .setSigningKey(decodedKey)
                    .build()
                    .parseClaimsJws(jwtToken);
            log.info(">>>>>> {}", claimsJws);
            String subject = claimsJws.getBody().getSubject();
            return new UserSession(Long.parseLong(subject));
        } catch (JwtException exception) {
            throw new UnAuthorizationException();
        }
    }
}


//    @Override
//    public Object resolveArgument(MethodParameter parameter,
//                                  ModelAndViewContainer mavContainer,
//                                  NativeWebRequest webRequest,
//                                  WebDataBinderFactory binderFactory) throws Exception {
//        HttpServletRequest servletRequest = webRequest.getNativeRequest(HttpServletRequest.class);
//        if (servletRequest == null) {
//            log.error("servletRequest null");
//            throw new UnAuthorizationException();
//        }
//
//        String accessToken = Arrays.stream(servletRequest.getCookies())
//                .filter(cookie -> cookie.getName().equals("SESSION"))
//                .map(Cookie::getValue)
//                .findFirst()
//                .orElseThrow(UnAuthorizationException::new);
//
//        Session session = sessionRepository.findByAccessToken(accessToken)
//                .orElseThrow(UnAuthorizationException::new);
//        return new UserSession(session.getUser().getId());
//    }
