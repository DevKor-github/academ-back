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

    // @Valid 어노테이션으로 수행한 유효성 검사 예외 처리
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDto> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e)
    {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(
                        ErrorDto.builder()
                                .data(null)
                                .message("잘못된 요청입니다.")
                                .version(versionProvider.getVersion())
                                .build()
                );
    }

    // 이메일 전송 시, 원인 불명으로 발생한 예외 처리
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorDto> runtimeExceptionHandler(RuntimeException e)
    {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(
                        ErrorDto.builder()
                                .data(null)
                                .message("예기치 못한 에러가 발생하였습니다.")
                                .version(versionProvider.getVersion())
                                .build()
                );
    }

    // 권한이 없는 api에 접근시 예외 처리
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> accessDeniedExceptionHandler(Exception e)
    {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(
                        ErrorDto.builder()
                                .data(null)
                                .message("권한이 없습니다.")
                                .version(versionProvider.getVersion())
                                .build()
                );
    }

}
