package com.example.vuelog.exception;

public class UnAuthorizationException extends RootException {

    private static final String EXCEPTION_MESSAGE = "need to authentication";

    public UnAuthorizationException() {
        super(EXCEPTION_MESSAGE);
    }

    public UnAuthorizationException(String fieldName, String message) {
        super(EXCEPTION_MESSAGE);
        addValidation(fieldName, message);
    }

    @Override
    public int getStatusCode() {
        return 401;
    }

}
