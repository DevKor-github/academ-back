package com.example.Devkor_project.exception;

import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionManager
{
    @ExceptionHandler(AppException.class)
    public ResponseEntity<String> appExceptionHandler(AppException e)
    {
        return ResponseEntity.status(e.getErrorCode().getHttpStatus())
                .body(e.getErrorCode() + ": " + e.getMessage());
    }
}
