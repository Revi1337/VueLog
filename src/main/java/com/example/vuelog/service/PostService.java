package com.example.vuelog.service;

import com.example.vuelog.domain.Post;
import com.example.vuelog.dto.request.PostCreate;
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
        Post post = postCreate.toEntity();
        postRepository.save(post);
    }

}
