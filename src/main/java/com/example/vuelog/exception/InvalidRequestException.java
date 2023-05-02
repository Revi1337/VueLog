package com.example.vuelog.exception;

import lombok.Getter;

/**
 * status -> 400
 */
@Getter
public class InvalidRequestException extends RootException {

    private static final String EXCEPTION_MESSAGE = "invalid request";

    public InvalidRequestException() {
        super(EXCEPTION_MESSAGE);
    }

    public InvalidRequestException(String fieldName, String message) {
        super(EXCEPTION_MESSAGE);
        addValidation(fieldName, message);
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}
