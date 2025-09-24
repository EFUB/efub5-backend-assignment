package com.efub.community.global.exception;

public enum ClientExceptionCode {
    // 전체
    INTERNAL_SERVER_ERROR,
    INVALID_PARAMETER,

    // Account
    MEMBER_NOT_FOUND,
    DUPLICATE_EMAIL,

    // Post
    POST_NOT_FOUND,
    POST_CONTENT_INVALID_LENGTH,
    POST_MEMBER_MISMATCH
}
