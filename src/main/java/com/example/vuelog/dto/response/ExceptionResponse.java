package com.example.vuelog.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Map;

@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
public record ExceptionResponse(
        int code,
        String message,
        Map<String, String> validation
) {

    public ExceptionResponse(int code, String message, Map<String, String> validation) {
        this.code = code;
        this.message = message;
        this.validation = validation != null ? validation : Map.of();
    }

    public static ExceptionResponse of(int code, String message, Map<String, String> validation) {
        return new ExceptionResponse(code, message, validation);
    }

    public static ExceptionResponse of(int code, String message) {
        return new ExceptionResponse(code, message, null);
    }

}
