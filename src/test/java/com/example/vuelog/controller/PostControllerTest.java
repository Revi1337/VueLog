package com.example.vuelog.controller;

import com.example.vuelog.domain.Post;
import com.example.vuelog.dto.request.PostCreate;
import com.example.vuelog.dto.request.PostEdit;
import com.example.vuelog.repository.PostRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest @AutoConfigureMockMvc
class PostControllerTest {

    private final MockMvc mockMvc;

    private final PostRepository postRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

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
                        .header(HttpHeaders.AUTHORIZATION, "revi1337")
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
                        .header(HttpHeaders.AUTHORIZATION, "revi1337")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\": \"title11\", \"content\": \"content11\"}"))
                .andExpect(status().isOk())
                .andDo(print());
        assertEquals(1L, postRepository.count());
        assertThat(postRepository.findAll()).extracting("title").containsExactly("title11");
    }

    @Test
    @DisplayName(value = "json 포맷을 직접작성하지 않고, ObjectMapper 로 해결")
    public void givenPostDataWhenRequestPostsThenStoredDataBase() throws Exception {
        String postCreateJson = objectMapper.writeValueAsString(PostCreate.builder()
                .title("Title")
                .content("content")
                .build());
        System.out.println("postCreateJson = " + postCreateJson);

        mockMvc.perform(post("/posts")
                        .header(HttpHeaders.AUTHORIZATION, "revi1337")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(postCreateJson))
                .andExpect(status().isOk())
                .andDo(print());
        assertEquals(1L, postRepository.count());
        assertThat(postRepository.findAll()).extracting("title").containsExactly("Title");
    }

    @Test
    @DisplayName(value = "글 한개 조회")
    public void searchOnePost() throws Exception {
        Post post = Post.builder().title("123456789012345").content("CONTENT").build();
        postRepository.save(post);

        mockMvc.perform(get("/posts/{postId}", post.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id" ).value(post.getId()))
                .andExpect(jsonPath("$.title" ).value("1234567890"))
                .andExpect(jsonPath("$.content").value("CONTENT"))
                .andDo(print());
    }

    @Test
    @DisplayName(value = "페이지를 0으로 요청하면 첫 페이지를 가져온다.")
    public void searchAllPost2() throws Exception {
        IntStream.range(1, 31).forEach(num -> postRepository
                .save(Post.builder()
                        .title("title" + num)
                        .content("content" + num)
                        .build()
                )
        );

        mockMvc.perform(get("/posts")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(10)))
                .andExpect(jsonPath("$.[0].id").value("31"))
                .andExpect(jsonPath("$.[0].title").value("title30"))
                .andExpect(jsonPath("$.[0].content").value("content30"))
                .andDo(print());
    }

    @Test
    @DisplayName(value = "글 제목 수정")
    public void updatePostTitle() throws Exception {
        Post post = Post.builder().title("타이틀").content("컨텐츠").build();
        postRepository.save(post);
        PostEdit postEdit = PostEdit.builder().title("수정타이틀").content("컨텐츠").build();

        mockMvc.perform(patch("/posts/{postId}", post.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postEdit)))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName(value = "글 삭제")
    public void deletePost() throws Exception {
        Post post = Post.builder().title("타이틀").content("컨텐츠").build();
        postRepository.save(post);

        mockMvc.perform(delete("/posts/{postId}", post.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName(value = "존재하지 않는 게시물을 요청했을때 익셉션")
    public void whenRequestNonExistsPost() throws Exception {
        Post post = Post.builder().title("타이틀").content("컨텐츠").build();
        postRepository.save(post);

        mockMvc.perform(get("/posts/{postId}", post.getId() + 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName(value = "존재하지 않는 게시물을 수정하는 요청했을때 익셉션")
    public void whenRequestUpdateNonExistsPost() throws Exception {
        PostEdit postEdit = PostEdit.builder().title("수정타이틀").content("컨텐츠").build();

        mockMvc.perform(patch("/posts/{postId}", + 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postEdit)))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @DisplayName(value = "게시물 작성 시 바보라는 글자를 포함될 수 없다")
    public void cannotContainSomeStringInRequest() throws Exception {
        String postCreateJson = objectMapper.writeValueAsString(PostCreate.builder()
                .title("바보")
                .content("content")
                .build());

        mockMvc.perform(post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(postCreateJson))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }
}