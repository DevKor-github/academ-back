package com.example.Devkor_project.exception;

import com.example.Devkor_project.configuration.VersionProvider;
import com.example.Devkor_project.dto.ErrorDto;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionManager
{
    @Autowired
    VersionProvider versionProvider;

    @ExceptionHandler(AppException.class)
    public ResponseEntity<ErrorDto> appExceptionHandler(AppException e)
    {
        return ResponseEntity.status(e.getErrorCode().getHttpStatus())
                .body(
                    ErrorDto.builder()
                            .data(e.getData())
                            .message(e.getErrorCode().getMessage())
                            .version(versionProvider.getVersion())
                            .build()
                );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDto> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e)
    {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(
                        ErrorDto.builder()
                                .status("error")
                                .data(null)
                                .message("잘못된 요청입니다.")
                                .version(versionProvider.getVersion())
                                .build()
                );
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorDto> runtimeExceptionHandler(RuntimeException e)
    {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(
                        ErrorDto.builder()
                                .status("error")
                                .data(null)
                                .message("예기치 못한 에러가 발생하였습니다.")
                                .version(versionProvider.getVersion())
                                .build()
                );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> accessDeniedExceptionHandler(Exception e)
    {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(
                        ErrorDto.builder()
                                .status("error")
                                .data(null)
                                .message("권한이 없습니다.")
                                .version(versionProvider.getVersion())
                                .build()
                );
    }

}
