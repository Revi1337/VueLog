package com.example.vuelog.config.mvc;

import com.example.vuelog.domain.Session;
import com.example.vuelog.exception.UnAuthorizationException;
import com.example.vuelog.repository.SessionRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Arrays;


@RequiredArgsConstructor @Slf4j
public class AuthResolver implements HandlerMethodArgumentResolver {

    private final SessionRepository sessionRepository;

    @Override // 1. Controller 에 UserSession 타입의 객체가 매개변수로 들어와있으면
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(UserSession.class);
    }

    @Override // 2. 인증 로직을 수행
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest servletRequest = webRequest.getNativeRequest(HttpServletRequest.class);
        if (servletRequest == null) {
            log.error("servletRequest null");
            throw new UnAuthorizationException();
        }

        String accessToken = Arrays.stream(servletRequest.getCookies())
                .filter(cookie -> cookie.getName().equals("SESSION"))
                .map(Cookie::getValue)
                .findFirst()
                .orElseThrow(UnAuthorizationException::new);

        Session session = sessionRepository.findByAccessToken(accessToken)
                .orElseThrow(UnAuthorizationException::new);
        return new UserSession(session.getUser().getId());
    }
}
