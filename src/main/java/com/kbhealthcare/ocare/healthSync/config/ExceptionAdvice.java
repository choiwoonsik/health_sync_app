package com.kbhealthcare.ocare.healthSync.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class ExceptionAdvice {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> undefinedException(Exception e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}

