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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.stream.IntStream;

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
    @DisplayName(value = """
            글 모두 조회. Pageable 매개변수를 지정한 Controller 단에서 직접 값이 넘어오지 않는 이상, yaml 에 one-indexed-parameters: true 를 설정해도
            page=0 이 첫번째 페이지다. --> Controller 단에서 직접 넘어와야, page=0,1 모두 첫번쨰 페이지를 돌려준다.""")
    public void searchAllPost() {
        Post post1 = Post.builder().title("TITLE1").content("CONTENT1").build();
        Post post2 = Post.builder().title("TITLE2").content("CONTENT2").build();
        Post post3 = Post.builder().title("TITLE3").content("CONTENT3").build();
        postRepository.saveAll(List.of(post1, post2, post3));

        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "id"));
        List<PostResponse> postResponse = postService.getList(pageRequest);

        assertThat(postRepository.count()).isEqualTo(3L);
        assertThat(postResponse).extracting("title").containsExactly("TITLE3", "TITLE2", "TITLE1");
    }

    @Test
    @DisplayName(value = "첫번째 페이지 조회")
    public void searchFirstPage() {
        IntStream.range(1, 31).forEach(num -> postRepository
                .save(Post.builder()
                        .title("title" + num)
                        .content("content" + num)
                        .build()
                )
        );

        PageRequest pageRequest = PageRequest.of(0, 5, Sort.Direction.DESC, "id");
        List<PostResponse> postResponse = postService.getList(pageRequest);

        assertEquals(5L, postResponse.size());
        assertThat(postResponse).extracting("title")
                .containsExactly("title30", "title29", "title28", "title27", "title26");
    }

}