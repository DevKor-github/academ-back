package com.example.Devkor_project.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode
{
    // App exception
    UNEXPECTED_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "예기치 못한 에러가 발생하였습니다."),
    EMAIL_DUPLICATED(HttpStatus.BAD_REQUEST, "해당 이메일은 이미 사용 중입니다."),
    EMAIL_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 이메일로 생성된 계정이 존재하지 않습니다."),
    CODE_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 이메일로 전송된 인증번호가 존재하지 않습니다."),
    WRONG_CODE(HttpStatus.BAD_REQUEST, "인증번호가 틀렸습니다."),
    SHORT_SEARCH_WORD(HttpStatus.BAD_REQUEST, "검색어는 2글자 이상이어야 합니다."),
    NO_RESULT(HttpStatus.NOT_FOUND, "검색 결과가 없습니다."),
    COURSE_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 course_id에 해당하는 강의가 존재하지 않습니다."),
    ALREADY_EXIST(HttpStatus.NOT_FOUND, "해당 강의에 해당 사용자는 이미 강의평을 달았습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
