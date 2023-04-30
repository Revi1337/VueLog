package com.example.vuelog.controller;

import com.example.vuelog.dto.request.PostCreate;
import com.example.vuelog.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


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

}
