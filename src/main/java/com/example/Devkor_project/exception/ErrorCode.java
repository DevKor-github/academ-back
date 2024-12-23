package com.example.Devkor_project.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode
{
    LOGIN_FAILURE(HttpStatus.BAD_REQUEST, "로그인에 실패하였습니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다."),
    UNEXPECTED_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "예기치 못한 에러가 발생하였습니다."),
    EMAIL_NOT_KOREA(HttpStatus.BAD_REQUEST, "해당 이메일은 고려대 이메일이 아닙니다."),
    INVALID_PURPOSE(HttpStatus.BAD_REQUEST, "해당 목적은 유효하지 않습니다."),
    EMAIL_DUPLICATED(HttpStatus.BAD_REQUEST, "해당 이메일은 이미 사용 중입니다."),
    EMAIL_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 이메일로 생성된 계정이 존재하지 않습니다."),
    CODE_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 이메일로 전송된 인증번호가 존재하지 않습니다."),
    WRONG_CODE(HttpStatus.BAD_REQUEST, "인증번호가 틀렸습니다."),
    INVALID_STUDENT_ID(HttpStatus.BAD_REQUEST, "해당 학번은 7자리가 아닙니다."),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "해당 비밀번호는 숫자, 영문이 포함된 8~24자리 글자가 아닙니다."),
    INVALID_USERNAME(HttpStatus.BAD_REQUEST, "해당 닉네임은 1~10자리 글자가 아닙니다."),
    INVALID_DEGREE(HttpStatus.BAD_REQUEST, "해당 학위는 'MASTER' 또는 'DOCTOR'이 아닙니다."),
    USERNAME_DUPLICATED(HttpStatus.BAD_REQUEST, "해당 닉네임은 이미 사용 중입니다."),
    SHORT_SEARCH_WORD(HttpStatus.BAD_REQUEST, "검색어는 2글자 이상이어야 합니다."),
    INVALID_ORDER(HttpStatus.BAD_REQUEST, "해당 결과 배치 순서는 유효하지 않습니다."),
    NO_RESULT(HttpStatus.NOT_FOUND, "결과가 없습니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 profile_id에 해당하는 사용자가 존재하지 않습니다."),
    COURSE_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 course_id에 해당하는 강의가 존재하지 않습니다."),
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 comment_id에 해당하는 강의평이 존재하지 않습니다."),
    COURSE_RATING_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 courseRating_id에 해당하는 평점이 존재하지 않습니다."),
    COMMENT_RATING_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 commentRating_id에 해당하는 평점이 존재하지 않습니다."),
    ALREADY_EXIST(HttpStatus.BAD_REQUEST, "해당 강의에 해당 사용자는 이미 강의평을 달았습니다."),
    NOT_COMMENT_BY_USER(HttpStatus.BAD_REQUEST, "해당 강의평은 해당 사용자가 작성한 강의평이 아닙니다."),
    SHORT_COMMENT_REVIEW(HttpStatus.BAD_REQUEST, "강의평 상세 내용은 50자 이상이어야 합니다."),
    LONG_COMMENT_REVIEW(HttpStatus.BAD_REQUEST, "강의평 상세 내용은 3000자 이하이어야 합니다."),
    INVALID_REASON(HttpStatus.BAD_REQUEST, "해당 신고 사유는 유효하지 않습니다."),
    TOO_MANY_REPORT(HttpStatus.BAD_REQUEST, "해당 강의평을 너무 많이 신고하였습니다."),
    INVALID_ITEM(HttpStatus.BAD_REQUEST, "해당 아이템은 유효하지 않습니다."),
    NOT_ENOUGH_POINT(HttpStatus.BAD_REQUEST, "포인트가 부족합니다."),
    WRONG_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호가 틀렸습니다."),
    TRAFFIC_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 기간 동안 요청이 들어오지 않았습니다."),
    UNKNOWN_NOT_FOUND(HttpStatus.NOT_FOUND, "알 수 없음 계정이 존재하지 않습니다."),
    INVALID_PERIOD(HttpStatus.BAD_REQUEST, "해당 교시는 유효하지 않습니다."),
    INVALID_TIME_LOCATION(HttpStatus.BAD_REQUEST, "해당 시간 및 장소 정보는 유효하지 않습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
