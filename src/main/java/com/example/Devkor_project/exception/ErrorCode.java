package com.example.Devkor_project.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode
{
    EMAIL_DUPLICATED(HttpStatus.BAD_REQUEST, ""),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, ""),
    INVALID_CODE(HttpStatus.UNAUTHORIZED, ""),
    UNEXPECTED_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, ""),
    EMAIL_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, ""),
    NO_AUTHORITY(HttpStatus.UNAUTHORIZED, ""),
    SHORT_SEARCH_WORD(HttpStatus.BAD_REQUEST, ""),
    NO_RESULT(HttpStatus.NOT_FOUND, "");

    private final HttpStatus httpStatus;
    private final String message;
}
