package com.example.vuelog.service;

import com.example.vuelog.domain.Post;
import com.example.vuelog.dto.request.PostCreate;
import com.example.vuelog.dto.response.PostResponse;
import com.example.vuelog.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class PostServiceTest {

    private final PostService postService;

    private final PostRepository postRepository;

    @Autowired
    public PostServiceTest(PostService postService, PostRepository postRepository) {
        this.postService = postService;
        this.postRepository = postRepository;
    }

    @BeforeEach
    public void beforeEach() {
        postRepository.deleteAll();
    }

    @Test
    @DisplayName(value = "글 작성")
    public void createPost() {
        PostCreate postCreate = PostCreate.builder()
                .title("제목입니다")
                .content("내용입니다")
                .build();
        postService.write(postCreate);
        assertEquals(1L, postRepository.count());
    }

    @Test
    @DisplayName(value = "글 한개 조회")
    public void searchOnePost() {
        Post post = Post.builder()
                .title("foo")
                .content("bar")
                .build();
        postRepository.save(post);

        PostResponse postResponse = postService.getPost(post.getId());

        assertNotNull(postResponse);
        assertEquals(1L, postRepository.count());
        assertEquals("foo", post.getTitle());
        assertEquals("bar", post.getContent());
    }

    @Test
    @DisplayName(value = "글 모두 조회")
    public void searchAllPost() {
        Post post1 = Post.builder().title("TITLE1").content("CONTENT1").build();
        Post post2 = Post.builder().title("TITLE2").content("CONTENT2").build();
        Post post3 = Post.builder().title("TITLE3").content("CONTENT3").build();
        postRepository.saveAll(List.of(post1, post2, post3));

        List<PostResponse> postResponse = postService.getList();

        assertThat(postRepository.count()).isEqualTo(3L);
        assertThat(postResponse).extracting("title").containsExactly("TITLE1", "TITLE2", "TITLE3");
    }

}