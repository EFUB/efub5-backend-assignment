package com.efub.community.member.dto;

import com.efub.community.member.domain.entity.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

// 회원가입 성공 Response DTO
@Builder
@Getter
public class CreateMemberResponseDto {
    private Long id;
    private String email;
    private String nickname;
    private String university;
    private String studentId;

    public static CreateMemberResponseDto from(Member member) {
        return CreateMemberResponseDto.builder()
                .id(member.getMemberId())
                .email(member.getEmail())
                .nickname(member.getNickname())
                .university(member.getUniversity())
                .studentId(member.getStudentId())
                .build();
    }
}