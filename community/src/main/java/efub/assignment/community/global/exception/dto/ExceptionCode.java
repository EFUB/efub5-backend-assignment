package efub.assignment.community.global.exception.dto;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ExceptionCode {
    // 400 Bad Request
    INVALID_PARAMETER(HttpStatus.BAD_REQUEST, ClientExceptionCode.INVALID_PARAMETER, "잘못된 인자입니다."),
    POST_CONTENT_INVALID_LENGTH(HttpStatus.BAD_REQUEST, ClientExceptionCode.POST_CONTENT_INVALID_LENGTH, "포스트 내용은 5자 이상 500자 이하여야 합니다."),
    BOARD_NAME_DUPLICATED(HttpStatus.BAD_REQUEST, ClientExceptionCode.BOARD_NAME_DUPLICATED, "이미 존재하는 게시판 이름입니다."),

    // 401 Unauthorized
    POST_ACCOUNT_MISMATCH(HttpStatus.UNAUTHORIZED, ClientExceptionCode.POST_ACCOUNT_MISMATCH, "허용되지 않은 계정의 접근입니다."),

    // 404 Not Found
    ACCOUNT_NOT_FOUND(HttpStatus.NOT_FOUND, ClientExceptionCode.ACCOUNT_NOT_FOUND, "계정이 존재하지 않습니다."),
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, ClientExceptionCode.POST_NOT_FOUND, "포스트가 존재하지 않습니다."),
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, ClientExceptionCode.COMMENT_NOT_FOUND, "댓글이 존재하지 않습니다."),
    LIKE_NOT_FOUND(HttpStatus.NOT_FOUND, ClientExceptionCode.LIKE_NOT_FOUND, "댓글 좋아요가 존재하지 않습니다."),
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, ClientExceptionCode.MEMBER_NOT_FOUND, "멤버가 존재하지 않습니다."),

    // 409 Conflict
    LIKE_ALREADY_EXISTS(HttpStatus.CONFLICT, ClientExceptionCode.LIKE_ALREADY_EXISTS, "이미 좋아요를 누르셨습니다."),

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