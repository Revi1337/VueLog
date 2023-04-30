package com.example.vuelog.controller;

import com.example.vuelog.dto.request.PostCreate;
import com.example.vuelog.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;


@Slf4j
@RestController @RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping(path = "/posts")
    public Map<String, String> post(@RequestBody @Valid PostCreate params) {
        postService.write(params);
        return Map.of();
    }

}
