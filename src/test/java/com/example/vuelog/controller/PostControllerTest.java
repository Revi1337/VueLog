package com.example.vuelog.controller;

import com.example.vuelog.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest @AutoConfigureMockMvc
class PostControllerTest {

    private final MockMvc mockMvc;

    private final PostRepository postRepository;

    @Autowired
    public PostControllerTest(MockMvc mockMvc, PostRepository postRepository) {
        this.mockMvc = mockMvc;
        this.postRepository = postRepository;
    }

    @BeforeEach
    @DisplayName(value = """
        전체 테스트를 돌리면 하나의 트랜잭션으로 모든 개별테스트가 돌아가기때문에 테스트메서드마다 값이 추가됨.
        따라서 beforeEach 로 각 테스트 메서드가 실행전에 테이블을 비워주게 설정한다.""")
    public void beforeEach() { postRepository.deleteAll(); }

    @Test
    @DisplayName(value = "/post 요청시 POST 를 출력한다.")
    public void controllerTest() throws Exception {
        mockMvc.perform(post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\": \"제목\", \"content\": \"내용\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("{}"))
                .andDo(print());
    }

    @Test
    @DisplayName(value = "/post 요청시 title 값은 필수다.")
    public void validTest() throws Exception {
        mockMvc.perform(post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\": null, \"content\": \"content\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.message").value("Invalid Request"))
                .andExpect(jsonPath("$.validation.title").value("specify title"))
                .andDo(print());
    }

    @Test
    @DisplayName(value = "/posts 요청 시 DB 에 값이 저장된다 1.")
    public void whenRequestPostsThenStoredInDatabase1() throws Exception {
        mockMvc.perform(post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\": \"title11\", \"content\": \"content11\"}"))
                .andExpect(status().isOk())
                .andDo(print());
        assertEquals(1L, postRepository.count());
    }

    @Test
    @DisplayName(value = "/posts 요청 시 DB 에 값이 저장된다 2.")
    public void whenRequestPostsThenStoredInDatabase2() throws Exception {
        mockMvc.perform(post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\": \"title11\", \"content\": \"content11\"}"))
                .andExpect(status().isOk())
                .andDo(print());
        assertEquals(1L, postRepository.count());
        assertThat(postRepository.findAll()).extracting("title").containsExactly("title11");
    }

}