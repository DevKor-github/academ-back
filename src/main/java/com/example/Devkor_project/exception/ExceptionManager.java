package com.example.Devkor_project.exception;

import com.example.Devkor_project.dto.ErrorDto;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDto> validRequestExceptionHandler(MethodArgumentNotValidException e)
    {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(
                        ErrorDto.builder()
                                .error_code("BAD_REQUEST")
                                .description("잘못된 요청입니다.")
                                .build()
                );
    }


}
