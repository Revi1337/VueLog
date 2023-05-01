package com.example.vuelog.dto.request;

import lombok.Builder;


@Builder
public record PostSearch(
    Integer page,
    Integer size
) {
    private static final int MAX_SIZE = 2000;

    public PostSearch(Integer page, Integer size) {
        this.page = page == null ? 0 : page;
        this.size = size == null ? 0 : size;
    }

    public long getOffset() {
        return (long) (Math.max(1, page) - 1) * Math.min(size, MAX_SIZE);
    }

}
