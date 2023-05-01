package com.example.vuelog.repository;

import com.example.vuelog.domain.Post;
import com.example.vuelog.dto.request.PostSearch;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.example.vuelog.domain.QPost.*;

@RequiredArgsConstructor
public class CustomPostRepositoryImpl implements CustomPostRepository{

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Post> getList(PostSearch postSearch) {
        return queryFactory.selectFrom(post)
                .offset(postSearch.getOffset())
                .limit(postSearch.size())
                .orderBy(post.id.desc())
                .fetch();
    }
}
