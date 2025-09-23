package com.efub.community.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
public class ErrorResponseDto {
    private String message;
    private int status;
}
