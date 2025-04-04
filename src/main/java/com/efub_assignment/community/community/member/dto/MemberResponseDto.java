package com.efub_assignment.community.community.member.dto;

import com.efub_assignment.community.community.member.domain.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberResponseDto {
    private String email;
    private String nickname;
    private String school;
    private String studentId;

public static MemberResponseDto from (Member member){
    return MemberResponseDto.builder()
            .email(member.getEmail())
            .nickname(member.getNickname())
            .school(member.getSchool())
            .studentId(member.getStudentId())
            .build();

    }
}
