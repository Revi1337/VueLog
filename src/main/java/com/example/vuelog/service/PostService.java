package com.example.vuelog.service;

import com.example.vuelog.domain.Post;
import com.example.vuelog.dto.request.PostCreate;
import com.example.vuelog.dto.response.PostResponse;
import com.example.vuelog.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
