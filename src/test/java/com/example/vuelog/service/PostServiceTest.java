package com.example.vuelog.service;

import com.example.vuelog.domain.Post;
import com.example.vuelog.dto.request.PostCreate;
import com.example.vuelog.dto.request.PostEdit;
import com.example.vuelog.dto.request.PostSearch;
import com.example.vuelog.dto.response.PostResponse;
import com.example.vuelog.exception.PostNotFoundException;
import com.example.vuelog.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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

        PostSearch postSearch = PostSearch.builder().page(1).size(10).build();
        List<PostResponse> postResponse = postService.getList(postSearch);

        assertThat(postRepository.count()).isEqualTo(3L);
        assertThat(postResponse).extracting("title").containsExactly("TITLE3", "TITLE2", "TITLE1");
    }

    @Test
    @DisplayName(value = "첫번째 페이지 조회")
    public void searchFirstPage() {
        IntStream.range(1, 21).forEach(num -> postRepository
                .save(Post.builder()
                        .title("title" + num)
                        .content("content" + num)
                        .build()
                )
        );

        PostSearch postSearch = PostSearch.builder().page(1).size(10).build();
        List<PostResponse> postResponse = postService.getList(postSearch);

        assertEquals(10L, postResponse.size());
        assertThat(postResponse.get(0)).extracting("title").isEqualTo("title20");
    }

    @Test
    @DisplayName(value = "글 제목 수정")
    public void updatePostTitle() {
        Post post = Post.builder().title("타이틀").content("컨텐츠").build();
        postRepository.save(post);
        PostEdit postEdit = PostEdit.builder().title("수정타이틀").build();

        postService.edit(post.getId(), postEdit);

        Post changedPost = postRepository.findById(post.getId())
                .orElseThrow(() -> new RuntimeException("post doesnt exists id : " + post.getId()));
        assertThat(changedPost).extracting("title").isEqualTo("수정타이틀");
    }

    @Test
    @DisplayName(value = """
             제목 수정시 컨텐츠 Null 테스트 PostEdit 에 content 를 추가안하면 기존의 content 가 날라감
             따라서 클라이언트와 update 시 기존의 데이터도 보낼것인지, 수정할 데이터만 보낼것인지 상의해야 함.
    """)
    public void update() {
        Post post = Post.builder().title("타이틀").content("컨텐츠").build();
        postRepository.save(post);
        PostEdit postEdit = PostEdit.builder().title("수정타이틀").content("컨텐츠").build();

        postService.edit(post.getId(), postEdit);

        Post changedPost = postRepository.findById(post.getId())
                .orElseThrow(() -> new RuntimeException("post doesnt exists id : " + post.getId()));
        assertThat(changedPost).extracting("title").isEqualTo("수정타이틀");
        assertThat(changedPost).extracting("content").isEqualTo("컨텐츠");
    }

    @Test
    @DisplayName(value = "글 내용 수정")
    public void updatePostContent() {
        Post post = Post.builder().title("타이틀").content("컨텐츠").build();
        postRepository.save(post);
        PostEdit postEdit = PostEdit.builder().title("타이틀").content("수정컨텐츠").build();

        postService.edit(post.getId(), postEdit);

        Post changedPost = postRepository.findById(post.getId())
                .orElseThrow(() -> new RuntimeException("post doesnt exists id : " + post.getId()));
        assertThat(changedPost).extracting("title").isEqualTo("타이틀");
        assertThat(changedPost).extracting("content").isEqualTo("수정컨텐츠");
    }

    @Test
    @DisplayName(value = "글 제목 혹은 내용 둘 중 하나만 수정")
    public void updatePostTitleOrContent() {
        Post post = Post.builder().title("타이틀").content("컨텐츠").build();
        postRepository.save(post);
        PostEdit postEdit = PostEdit.builder().content("수정컨텐츠").build();

        postService.edit(post.getId(), postEdit);

        Post changedPost = postRepository.findById(post.getId())
                .orElseThrow(() -> new RuntimeException("post doesnt exists id : " + post.getId()));
        assertThat(changedPost).extracting("title").isEqualTo("타이틀");
        assertThat(changedPost).extracting("content").isEqualTo("수정컨텐츠");
    }

    @Test
    @DisplayName(value = "게시글 삭제")
    public void deletePost() {
        Post post = Post.builder().title("타이틀").content("컨텐츠").build();
        postRepository.save(post);

        postService.delete(post.getId());

        assertThat(postRepository.count()).isEqualTo(0);
    }

    @Test
    @DisplayName(value = "게시글 조회 - 존재하지 않는 게시글을조회하면 익셉션이 터져야한다.")
    public void whenSearchNonExistPost() {
        Post post = Post.builder().title("타이틀").content("컨텐츠").build();
        postRepository.save(post);

        assertThrows(PostNotFoundException.class, () -> postService.getPost(post.getId() + 1L));
    }

    @Test
    @DisplayName(value = "게시글 수정 - 존재하지 않는 게시글을조회하면 익셉션이 터져야한다.")
    public void whenUpdateNonExistPost() {
        Post post = Post.builder().title("타이틀").content("컨텐츠").build();
        postRepository.save(post);

        assertThrows(PostNotFoundException.class,
                () -> postService.edit(post.getId() + 1L, new PostEdit("asd", "Asd")));
    }

    @Test
    @DisplayName(value = "게시글 삭제 - 존재하지 않는 게시글을조회하면 익셉션이 터져야한다.")
    public void whenDeleteNonExistPost() {
        Post post = Post.builder().title("타이틀").content("컨텐츠").build();
        postRepository.save(post);

        assertThrows(PostNotFoundException.class,
                () -> postService.delete(post.getId() + 1L));
    }

}