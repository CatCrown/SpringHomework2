package com.sparta.springhomework2.handler;

import com.sparta.springhomework2.dto.StatusResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public StatusResponseDto<?> handlerException(Exception ex) {
        return StatusResponseDto.fail(500, ex.getMessage());
    }

    @ExceptionHandler({IllegalAccessException.class, NullPointerException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public StatusResponseDto<?> handle(Exception ex) {
        return StatusResponseDto.fail(400, ex.getMessage());
    }
}