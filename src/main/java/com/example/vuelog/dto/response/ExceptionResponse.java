package com.example.vuelog.dto.response;

import java.util.Map;

public record ExceptionResponse(
        int code,
        String message,
        Map<String, String> validation
) {

    public static ExceptionResponse of(int code, String message, Map<String, String> validation) {
        return new ExceptionResponse(code, message, validation);
    }

}
