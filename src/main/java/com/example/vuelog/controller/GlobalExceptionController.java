package com.example.vuelog.controller;

import com.example.vuelog.dto.response.ExceptionResponse;
import com.example.vuelog.exception.RootException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.*;


@ControllerAdvice @Slf4j
public class GlobalExceptionController {

    @ResponseBody
    @ResponseStatus(value = BAD_REQUEST)
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ExceptionResponse invalidExceptionHandler(MethodArgumentNotValidException exception) {
        Map<String, String> fieldMessages = !exception.hasFieldErrors() ? Map.of() :  exception
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        Objects.requireNonNull(DefaultMessageSourceResolvable::getDefaultMessage)));
        return ExceptionResponse.of(BAD_REQUEST.value(), "Invalid Request", fieldMessages);
    }

    @ResponseBody
    @ExceptionHandler(value = RootException.class)
    public ResponseEntity<ExceptionResponse> rootExceptionHandler(RootException exception) {
        Map<String, String> validation = exception.getValidation();
        return ResponseEntity
                .status(exception.getStatusCode())
                .body(ExceptionResponse.of(exception.getStatusCode(), exception.getMessage(), validation));
    }
}

