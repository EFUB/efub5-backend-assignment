package com.efub.community.post.domain;

import com.efub.community.global.domain.BaseEntity;
import com.efub.community.member.domain.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 내용
    private String content;

    // 글쓴이
    @ManyToOne(fetch = FetchType.LAZY)
    private Member writer;

    // 글쓴이가 설정한 닉네임
    private String nickname;

    // 익명 여부
    private Boolean anonymous;

    // 빌더
    @Builder
    public Post(String content, Boolean anonymous, String nickname, Member writer) {
        this.content = content;
        this.anonymous = anonymous;
        this.nickname = nickname;
        this.writer = writer;
    }

    // 게시물 내용 수정
    public void changeContent(String newContent) {
        this.content = newContent;
    }
}
