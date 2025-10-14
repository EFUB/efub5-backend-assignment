package efub.assignment.community.global.exception.dto;

public enum ClientExceptionCode {
    // 전체
    INTERNAL_SERVER_ERROR,
    INVALID_PARAMETER,

    // account
    ACCOUNT_NOT_FOUND,

    // Post
    POST_NOT_FOUND,
    POST_CONTENT_INVALID_LENGTH,
    POST_ACCOUNT_MISMATCH,

    // Board
    BOARD_NOT_FOUND,
    BOARD_NAME_DUPLICATED,

    // Comment
    COMMENT_NOT_FOUND,

    // CommentLike
    LIKE_NOT_FOUND,
    LIKE_ALREADY_EXISTS,

    // Member
    MEMBER_NOT_FOUND,
}
