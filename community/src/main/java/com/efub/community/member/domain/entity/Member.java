package com.efub.community.member.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "members")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    // 회원 이메일 - 중복 X
    @Column(unique=true)
    private String email;

    // 회원 비밀번호
    @Column(nullable = false)
    private String password;

    // 회원 닉네임
    @Column (nullable = false)
    private String nickname;

    // 대학
    @Column(nullable = false)
    private String university;

    // 학번
    @Column(nullable = false)
    private String studentId;

    // 회원 상태
    @Enumerated(EnumType.STRING)
    private MemberStatus status = MemberStatus.ACTIVE;

    @Builder // setter 지양.
    public Member(String email, String password, String nickname, String university, String studentId) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.university = university;
        this.studentId = studentId;
    }

    // 회원 정보 수정
    public void updateMember(String email, String nickname, String university, String studentId) {
        this.email = email;
        this.nickname = nickname;
        this.university = university;
        this.studentId = studentId;
    }

    public void changeStatus(MemberStatus status) {
        this.status = status;
    }
}
