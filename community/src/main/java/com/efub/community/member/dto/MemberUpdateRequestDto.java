package com.efub.community.member.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

// 회원 정보 수정 요청 DTO
@Getter
@NoArgsConstructor
public class MemberUpdateRequestDto {

    @NotBlank(message = "이메일은 필수 항목입니다.")
    @Email(message = "유효한 이메일 형식이 아닙니다.")
    private String email;

    @NotBlank(message = "닉네임은 필수 항목입니다.")
    @Size(min = 2, max = 10, message = "닉네임은 2자 이상 10자 이하이어야 합니다.")
    private String nickname;

    @NotBlank(message = "대학 이름은 필수 항목입니다.")
    private String university;

    @NotBlank(message = "학번은 필수 항목입니다.")
    private String studentId;
}