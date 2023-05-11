package com.example.vuelog.config.mvc;

import com.example.vuelog.config.AppConfig;
import com.example.vuelog.repository.SessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration @RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private final SessionRepository sessionRepository;

    private final AppConfig appConfig;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new AuthResolver(sessionRepository, appConfig));
    }
}

//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(new AuthInterceptor())
//                .excludePathPatterns("/error");                                  // 해당 Path 에만 설정한 Interceptor 가 동작한다. (excludePathPatterns 도 있음.)
//    }
