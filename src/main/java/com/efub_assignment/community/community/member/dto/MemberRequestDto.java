package com.efub_assignment.community.community.member.dto;

import com.efub_assignment.community.community.member.domain.Member;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberRequestDto {


    @NotBlank // @NotNull은 문자열 공백은 허용됨
    private String email;

    @NotBlank
    private String password;

    @NotBlank
    private String nickname;

    @NotBlank
    private String school;

    @NotBlank
    private String studentId;

    //Member 객체로 build
    public Member toEntity(){
        return Member.builder()
                .email(email)
                .password(password)
                .nickname(nickname)
                .school(school)
                .studentId(studentId)
                .build();
    }
}
