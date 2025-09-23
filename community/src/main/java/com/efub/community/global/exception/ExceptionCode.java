package com.efub.community.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ExceptionCode {
    // 400 Bad Request
    INVALID_PARAMETER(HttpStatus.BAD_REQUEST, ClientExceptionCode.INVALID_PARAMETER, "잘못된 인자입니다."),
    POST_CONTENT_INVALID_LENGTH(HttpStatus.BAD_REQUEST, ClientExceptionCode.POST_CONTENT_INVALID_LENGTH, "포스트 내용은 5자 이상 500자 이하여야 합니다."),

    // 401 Unauthorized
    POST_MEMBER_MISMATCH(HttpStatus.UNAUTHORIZED, ClientExceptionCode.POST_MEMBER_MISMATCH, "허용되지 않은 계정의 접근입니다."),

    // 404 Not Found
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, ClientExceptionCode.MEMBER_NOT_FOUND, "계정이 존재하지 않습니다."),
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, ClientExceptionCode.POST_NOT_FOUND, "포스트가 존재하지 않습니다."),

    // 500 Internal Server Error
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, ClientExceptionCode.INTERNAL_SERVER_ERROR, "예상치 못한 서버에러가 발생했습니다.");

    private final HttpStatus httpStatus;
    private final ClientExceptionCode clientExceptionCode;
    private final String message;

    ExceptionCode(HttpStatus httpStatus, ClientExceptionCode clientExceptionCode, String message) {
        this.httpStatus = httpStatus;
        this.clientExceptionCode = clientExceptionCode;
        this.message = message;
    }
}
