package com.example.vuelog.service;

import com.example.vuelog.domain.Post;
import com.example.vuelog.domain.PostEditor;
import com.example.vuelog.dto.request.PostCreate;
import com.example.vuelog.dto.request.PostEdit;
import com.example.vuelog.dto.request.PostSearch;
import com.example.vuelog.dto.response.PostResponse;
import com.example.vuelog.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service @Transactional @RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public void write(PostCreate postCreate) {
        postRepository.save(postCreate.toEntity());
    }

    public PostResponse getPost(Long id) {
        Post findPost = postRepository
                .findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not exists posts"));
        return PostResponse.from(findPost);
    }

    public List<PostResponse> getList(PostSearch postSearch) {
        return postRepository.getList(postSearch)
                .stream()
                .map(PostResponse::from)
                .toList();
    }

    public void edit(Long id, PostEdit postEdit) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("post doesnt exists"));
        PostEditor.PostEditorBuilder editorBuilder = post.toEditor();
        PostEditor postEditor = editorBuilder.title(postEdit.title()).content(postEdit.content()).build();
        post.edit(postEditor);
    }

    public void delete(Long id) {
        postRepository.delete(
                postRepository.findById(id)
                        .orElseThrow(() -> new IllegalArgumentException("post doesnt exists"))
        );
    }
}
