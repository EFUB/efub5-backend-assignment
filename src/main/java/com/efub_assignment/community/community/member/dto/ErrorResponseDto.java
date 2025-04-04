package com.efub_assignment.community.community.member.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ErrorResponseDto {
    private int status;          // HTTP 상태 코드
    private String error;        // 에러 유형
    private String message;      // 상세 에러 메시지
    private String path;         // 요청 경로
    private LocalDateTime timestamp; // 에러 발생 시간

    public static ErrorResponseDto of(int status, String error, String message, String path) {
        return ErrorResponseDto.builder()
                .timestamp(LocalDateTime.now())
                .status(status)
                .error(error)
                .message(message)
                .path(path)
                .build();
    }
}
