package com.example.vuelog.config.mvc;

import com.example.vuelog.exception.UnAuthorizationException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

// TODO Interceptor 는 컨트롤러 진입하기 전에 실행되며 Controller 로 넘겨주는 정보의 가공하는 역할을 함
//  Filter 는 DispatcherServlet 이 실행되기 전, (스프링 컨테이너에 들어오기 전)
//  Interceptor 는 DispatcherServlet 이 실행된 후에 호출되며 Interceptor 는 DispatcherServlet 이 실행되며 호출된다. (스프링 컨테이너에 들어온 후)
//  --> Interceptor 를 동록하려면 WebMvcConfigurer 를 impl 받고 addInterceptors 를 오버라이딩해서 Interceptor 를 등록해주어야한다.
//  --> 또한 WebMvcConfigurer 에서 직접 만든 Interceptor 가 실행할 경로를 설정해주지 않으면 모든 요청마다 직접 만든 Interceptor 가 동작한다.
//  REFER : https://gngsn.tistory.com/153 , https://gngsn.tistory.com/83

@Slf4j
public class AuthInterceptor implements HandlerInterceptor {

    @Override // Controller 가 동작 직전 ( Object handler 는 현재 실행하려는 메소드 자체를 의미함 --> 여기가 Controller 로 넘어갈지 말지에 대한 포인트임 --> false 를 주면 Controller 로 넘어가지 않음.)
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("AuthInterceptor.preHandle");
        String accessToken = request.getParameter("accessToken");
        if (accessToken != null && accessToken.equals("revi1337"))
            return true;
        throw new UnAuthorizationException();
    }

    @Override // Controller 를 거치고 난 후
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.info("AuthInterceptor.postHandle");
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override // DispatcherServlet 의 화면 처리(뷰)가 완료된 상태에서 처리
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        log.info("AuthInterceptor.afterCompletion");
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
