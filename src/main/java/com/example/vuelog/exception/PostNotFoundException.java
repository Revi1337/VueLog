package com.example.vuelog.exception;

public class PostNotFoundException extends RuntimeException {

    private static final String EXCEPTION_MESSAGE = "non exists posts";

    public PostNotFoundException() {
        super();
    }

    public PostNotFoundException(String message) {
        super(EXCEPTION_MESSAGE);
    }

    public PostNotFoundException(String message, Throwable cause) {
        super(EXCEPTION_MESSAGE, cause);
    }

    public PostNotFoundException(Throwable cause) {
        super(cause);
    }

    protected PostNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(EXCEPTION_MESSAGE, cause, enableSuppression, writableStackTrace);
    }
}
