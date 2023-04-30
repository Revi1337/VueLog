package com.example.vuelog.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PostTest {

    @Test
    @DisplayName(value = "Builder 패턴 테스트")
    public void testBuilderPattern() {
        Post build = Post.builder().build();
        System.out.println("build = " + build);
    }

}