package com.example.Devkor_project.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode
{
    EMAIL_DUPLICATED(HttpStatus.BAD_REQUEST, ""),
    EMAIL_Not_Found(HttpStatus.NOT_FOUND, ""),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, ""),;

    private HttpStatus httpStatus;
    private String message;
}
