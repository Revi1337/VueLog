package com.example.vuelog.dto.request;

import org.junit.jupiter.api.Test;


class PostSearchTest {

    @Test
    public void asdf() {
        PostSearch postSearch = PostSearch.builder().build();
        System.out.println("postSearch.size() = " + postSearch.size());
        System.out.println("postSearch.page() = " + postSearch.page());
    }

}