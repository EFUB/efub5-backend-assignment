package com.efub_assignment.community.community.member.domain;

import com.efub_assignment.community.community.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Builder;

@Entity
@Getter
@Table(name = "members")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity {

    // 멤버 아이디
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    // 멤버 이메일
    @Column(unique = true)
    private String email;

    // 멤버 학번
    @Column(unique = true)
    private String studentId;

    // 멤버 학교
    @Column
    private String school;

    // 멤버 닉네임
    @Column(nullable = false)
    private String nickname;

    //멤버 비밀번호
    @Column(unique = true, nullable = false)
    private String password;

    //멤버 상태
    @Enumerated(EnumType.STRING)
    private MemberStatus status = MemberStatus.RESISTER;

    @Builder
    public Member(String email, String studentId, String school, String nickname, String password){
        this.email = email;
        this.studentId = studentId;
        this.school = school;
        this.nickname = nickname;
        this.password = password;
    }

    public void updateNickname(String nickname){
        this.nickname = nickname;
    }

    public void changeStatus(MemberStatus status){
        this.status = status;
    }


}
