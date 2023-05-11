package com.example.vuelog.config.mvc;

import com.example.vuelog.domain.Session;
import com.example.vuelog.exception.UnAuthorizationException;
import com.example.vuelog.repository.SessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;


@RequiredArgsConstructor
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
        String accessToken = webRequest.getHeader(HttpHeaders.AUTHORIZATION);
        if (accessToken == null || accessToken.isBlank())
            throw new UnAuthorizationException();
        Session session = sessionRepository.findByAccessToken(accessToken)
                .orElseThrow(UnAuthorizationException::new);
        return new UserSession(session.getUser().getId());
    }
}
