package com.example.vuelog.exception;

/**
 * status -> 404
 */
public class PostNotFoundException extends RootException {

    private static final String EXCEPTION_MESSAGE = "non exists posts";

    public PostNotFoundException() {
        super(EXCEPTION_MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 404;
    }
}
