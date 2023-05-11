package com.example.vuelog.controller;

import com.example.vuelog.domain.Session;
import com.example.vuelog.domain.User;
import com.example.vuelog.dto.request.LoginRequest;
import com.example.vuelog.repository.SessionRepository;
import com.example.vuelog.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest @AutoConfigureMockMvc
class AuthControllerTest {

    private final MockMvc mockMvc;

    private final UserRepository userRepository;

    private final SessionRepository sessionRepository;

    @PersistenceContext EntityManager entityManager;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    public AuthControllerTest(MockMvc mockMvc, UserRepository userRepository, SessionRepository sessionRepository) {
        this.mockMvc = mockMvc;
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
    }

    @BeforeEach
    public void beforeEach() {
        userRepository.deleteAll();
        sessionRepository.deleteAll();
    }

    @Test
    @DisplayName(value = "로그인 성공")
    public void successLoginTest() throws Exception {
        User user = User.create().name("revi1337").email("revi1337@naver.com").password("1234").build();
        userRepository.save(user);

        LoginRequest loginRequest = LoginRequest.builder().email("revi1337@naver.com").password("1234").build();
        String json = objectMapper.writeValueAsString(loginRequest);

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test @Transactional @Rollback
    @DisplayName(value = "로그인 성공 후, 세션 1개 생성")
    public void createSessionAfterLoginTest() throws Exception {
        User user = User.create().name("revi1337").email("revi1337@naver.com").password("1234").build();
        userRepository.save(user);

        LoginRequest loginRequest = LoginRequest.builder().email("revi1337@naver.com").password("1234").build();
        String json = objectMapper.writeValueAsString(loginRequest);

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andDo(print());

        entityManager.flush();
        entityManager.clear();

        List<Session> resultList = entityManager.createQuery("select s from Session as s left join fetch s.user as u", Session.class)
                .getResultList();
        for (Session session : resultList) {
            System.out.println("session = " + session.getUser());
        }
        assertEquals(1L, sessionRepository.count());
    }

    @Test
    @DisplayName(value = "로그인 성공 후, 세션 응답")
    public void createSessionAfterLoginTest2() throws Exception {
        User user = User.create().name("revi1337").email("revi1337@naver.com").password("1234").build();
        userRepository.save(user);

        LoginRequest loginRequest = LoginRequest.builder().email("revi1337@naver.com").password("1234").build();
        String json = objectMapper.writeValueAsString(loginRequest);

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken", Matchers.notNullValue()))
                .andDo(print());
    }

    @Test
    @DisplayName(value = "로그인 후 권한이 필요한 페이지를 접속한다.")
    public void createSessionAfterLoginTest3() throws Exception {
        User user = User.create().name("revi1337").email("revi1337@naver.com").password("1234").build();
        Session session = user.addSession();
        userRepository.save(user);

        mockMvc.perform(get("/foo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, session.getAccessToken()))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName(value = "로그인 후 검증되지 않은 세션값으로 권한이 필요한 페이지에 접속할 수 없다.")
    public void createSessionAfterLoginTest4() throws Exception {
        User user = User.create().name("revi1337").email("revi1337@naver.com").password("1234").build();
        Session session = user.addSession();
        userRepository.save(user);

        mockMvc.perform(get("/foo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, session.getAccessToken() + "exploit"))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

}