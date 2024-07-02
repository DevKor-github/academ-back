package com.example.Devkor_project.exception;

import com.example.Devkor_project.configuration.VersionProvider;
import com.example.Devkor_project.dto.ResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionManager {
        @Autowired
        VersionProvider versionProvider;

        @ExceptionHandler(AppException.class)
        public ResponseEntity<ResponseDto.Error> appExceptionHandler(AppException e) {
                return ResponseEntity.status(e.getErrorCode().getHttpStatus())
                        .body(
                                ResponseDto.Error.builder()
                                        .code(e.getErrorCode().toString())
                                        .message(e.getErrorCode().getMessage())
                                        .data(e.getData())
                                        .version(versionProvider.getVersion())
                                        .build());
        }

        // @Valid 어노테이션으로 수행한 유효성 검사 예외 처리
        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<ResponseDto.Error> methodArgumentNotValidExceptionHandler(
                        MethodArgumentNotValidException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(
                                ResponseDto.Error.builder()
                                        .code("INVALID_DATA")
                                        .message("잘못된 형식의 요청 데이터입니다.")
                                        .data(null)
                                        .version(versionProvider.getVersion())
                                        .build());
        }

        // 원인 불명으로 발생한 예외 처리
        @ExceptionHandler(Exception.class)
        public ResponseEntity<ResponseDto.Error> unexpectedExceptionHandler(Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(
                                ResponseDto.Error.builder()
                                        .code("UNEXPECTED_ERROR")
                                        .message(e.getMessage())
                                        .data(null)
                                        .version(versionProvider.getVersion())
                                        .build());
        }
}
