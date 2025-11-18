package com.efub_assignment.community.community.querydsl;

import com.efub_assignment.community.community.board.domain.Board;
import com.efub_assignment.community.community.board.repository.BoardRepository;
import com.efub_assignment.community.community.member.domain.Member;
import com.efub_assignment.community.community.member.repository.MemberRepository;
import com.efub_assignment.community.community.post.domain.Post;
import com.efub_assignment.community.community.post.repository.PostRepository;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class QueryDslTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    BoardRepository boardRepository;

    @Autowired
    PostRepository postRepository;

    @BeforeEach
    void cleanUp() {
        postRepository.deleteAll();
        boardRepository.deleteAll();
        memberRepository.deleteAll();
    }

    @Test
    void insertDataForSearch() {

        // 1. 게시판 생성자(Owner) 생성
        Member owner = memberRepository.save(
                Member.builder()
                        .email("owner@example.com")
                        .password("1234")
                        .nickname("boardOwner")
                        .school("이화여대")
                        .studentId("20250000")
                        .build()
        );

        // 2. 게시판 생성
        Board board = boardRepository.save(
                Board.builder()
                        .boardName("자유게시판")
                        .description("검색 테스트용 게시판")
                        .notice("공지 없음")
                        .password("abcd1234")
                        .owner(owner)
                        .build()
        );

        // 3. 일반 회원 생성
        Member m1 = memberRepository.save(
                Member.builder()
                        .email("ewha1@example.com")
                        .password("1111")
                        .nickname("ewhaaaaaaa")
                        .school("이화여대")
                        .studentId("20250001")
                        .build()
        );

        Member m2 = memberRepository.save(
                Member.builder()
                        .email("fubi1@example.com")
                        .password("2222")
                        .nickname("fubiiiiiii")
                        .school("이화여대")
                        .studentId("20250002")
                        .build()
        );

        // 4. 게시글 데이터 삽입
        postRepository.save(Post.builder()
                .title("eWHa")
                .content("good")
                .writer(m1)
                .board(board)
                .build());

        postRepository.save(Post.builder()
                .title("cat or dog")
                .content("cat")
                .writer(m1)
                .board(board)
                .build());

        postRepository.save(Post.builder()
                .title("univ")
                .content("EWHA")
                .writer(m2)
                .board(board)
                .build());

        postRepository.save(Post.builder()
                .title("ewha university")
                .content("efub")
                .writer(m2)
                .board(board)
                .build());
    }

}
