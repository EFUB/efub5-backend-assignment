package com.efub_assignment.community.community.member.domain;

import com.efub_assignment.community.community.comment.domain.Comment;
import com.efub_assignment.community.community.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Builder;

import java.util.ArrayList;
import java.util.List;

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

    //멤버별 댓글 리스크
    @OneToMany(mappedBy = "writer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> commnetList = new ArrayList<>();

    @Builder
    public Member(String email, String studentId, String school, String nickname, String password){
        this.email = email;
        this.studentId = studentId;
        this.school = school;
        this.nickname = nickname;
        this.password = password;
    }

    public void updateNickname(String nickname){this.nickname = nickname; // 닉네임을 변경하는 로직 추가
        }

    public void changeStatus(MemberStatus status){
        this.status = status;
    }


}
