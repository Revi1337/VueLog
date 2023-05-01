package com.example.vuelog.repository;

import com.example.vuelog.domain.Post;
import com.example.vuelog.dto.request.PostSearch;

import java.util.List;

public interface CustomPostRepository {

    List<Post> getList(PostSearch postSearch);
}
