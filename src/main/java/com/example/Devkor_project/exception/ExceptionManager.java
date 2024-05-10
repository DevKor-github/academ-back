package com.example.Devkor_project.exception;

import com.example.Devkor_project.dto.ErrorDto;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionManager
{
    @ExceptionHandler(AppException.class)
    public ResponseEntity<ErrorDto> appExceptionHandler(AppException e)
    {
        return ResponseEntity.status(e.getErrorCode().getHttpStatus())
                .body(
                    ErrorDto.builder()
                            .error_code(e.getErrorCode().name())
                            .description(e.getErrorCode().getDescription())
                            .build()
                );
    }
}
