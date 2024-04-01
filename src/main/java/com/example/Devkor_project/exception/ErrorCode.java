package com.example.Devkor_project.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode
{
    EMAIL_DUPLICATED(HttpStatus.BAD_REQUEST, "");

    private HttpStatus httpStatus;
    private String message;
}
