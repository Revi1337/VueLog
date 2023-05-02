package com.example.vuelog.controller;

import com.example.vuelog.dto.request.PostCreate;
import com.example.vuelog.dto.request.PostEdit;
import com.example.vuelog.dto.request.PostSearch;
import com.example.vuelog.dto.response.PostResponse;
import com.example.vuelog.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@RestController @RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping(path = "/posts")
    public ResponseEntity<Void> post(@RequestBody @Valid PostCreate params) {
        postService.write(params);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }

    @GetMapping(path = "/posts/{postId}")
    public ResponseEntity<PostResponse> getPost(@PathVariable(name = "postId") Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(postService.getPost(id));
    }

    @GetMapping(path = "/posts")
    public ResponseEntity<List<PostResponse>> postAll(PostSearch postSearch) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(postService.getList(postSearch));
    }

    @PatchMapping(path = "/posts/{postId}")
    public ResponseEntity<Void> updatePost(
            @PathVariable(name = "postId") Long id, @RequestBody @Valid PostEdit postEdit) {
        postService.edit(id, postEdit);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @DeleteMapping(path = "/posts/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable(name = "postId") Long id) {
        postService.delete(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }


}
