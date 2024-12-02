package com.chatop.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    static final String STATUS = "status";
    static final String MESSAGE = "message";
    static final String TIMESTAMP = "timestamp";

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Object> handleNotFoundException(NotFoundException exception) {
        return this.createResponseEntity(HttpStatus.NOT_FOUND, exception.getMessage());
    }


    private ResponseEntity<Object> createResponseEntity(HttpStatus status, String message) {
        Map<String, Object> body = new HashMap<>();
        body.put(STATUS, status.value());
        body.put(MESSAGE, message);
        body.put(TIMESTAMP, LocalDateTime.now());

        return new ResponseEntity<>(body, status);
    }
}
