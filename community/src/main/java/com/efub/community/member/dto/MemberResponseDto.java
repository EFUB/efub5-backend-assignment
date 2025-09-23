package com.efub.community.member.dto;

import com.efub.community.member.domain.entity.Member;
import lombok.Builder;
import lombok.Getter;

@Builder @Getter
public class MemberResponseDto {

    private String nickname;
    private String email;
    private String university;
    private String studentId;

    public static MemberResponseDto from(Member member) {
        return MemberResponseDto.builder()
                .nickname(member.getNickname())
                .email(member.getEmail())
                .university(member.getUniversity())
                .studentId(member.getStudentId())
                .build();
    }
}