package com.efub.community.board.domain;

import com.efub.community.global.domain.BaseEntity;
import com.efub.community.member.domain.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity // 이 클래스가 JPA 엔티티임을 나타냄. DB 테이블에 매핑
@Getter // 롬복 라이브러리로 모든 필드의 getter 메서드를 자동 생성
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 기본 생성자 자동 생성 + 접근 제어자 설정
public class Board extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 게시판 제목
    private  String title;

    // 게시판 설명
    private String description;

    // 게시판 공지
    private String notice;

    // 게시판 주인
    @ManyToOne(fetch = FetchType.LAZY) // 일대다 + 지연로딩
    private Member owner;

    @Builder
    public Board(String title, String description, String notice, Member owner) {
        this.title = title;
        this.description = description;
        this.notice = notice;
        this.owner = owner;
    }

    // 게시판지기 수정
    public void changeOwner(Member newOwner) {
        this.owner = newOwner;
    }
}
