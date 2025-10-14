package com.efub_assignment.community.community.post.controller;

import com.efub_assignment.community.community.board.domain.Board;
import com.efub_assignment.community.community.board.repository.BoardRepository;
import com.efub_assignment.community.community.member.domain.Member;
import com.efub_assignment.community.community.member.repository.MemberRepository;
import com.efub_assignment.community.community.post.dto.request.PostCreateRequest;
import com.efub_assignment.community.community.post.repository.PostRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MemberRepository memberRepository; // 테스트 데이터 생성을 위함

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private PostRepository postRepository;

    private Member testMember;
    private Board testBoard;
    private String testMemberPassword = "password";

    @BeforeEach
    void setUp(){
        testMember = Member.builder()
                .nickname("user")
                .email("user@email.com")
                .password(testMemberPassword)
                .build();
        memberRepository.save(testMember);

        testBoard = Board.builder()
                .boardName("게시판")
                .description("post")
                .owner(testMember)
                .build();
        boardRepository.save(testBoard);

    }

    @Test
    @DisplayName("게시물 생성 성공")
    void createPost_Success() throws Exception {
        //given
        PostCreateRequest request = new PostCreateRequest(testMember.getMemberId(), testBoard.getId(), "제목", "테스트 입니다.");
        String requestBody = objectMapper.writeValueAsString(request);

        //when
        ResultActions resultActions = mockMvc.perform(
                post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        );

        //then

        resultActions
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andDo(print());
    }

    @Test
    @DisplayName("게시물 생성 실패 - 존재하지 않는 사용자로 생성 시도")
    void createPost_Fail_MemberNotFound() throws Exception {
        // given
        Long nonExistentMemberId = 999L; // DB에 존재하지 않는 임의의 회원 ID
        // PostCreateRequest에 유효한 boardId와 유효하지 않은 memberId를 포함
        PostCreateRequest requestDto = new PostCreateRequest(
                nonExistentMemberId,
                testBoard.getId(), // setUp에서 만든 유효한 게시판 ID
                "실패 테스트 제목",
                "실패 테스트 내용입니다. 5자는 넘습니다."
        );
        String requestBody = objectMapper.writeValueAsString(requestDto);

        // when
        ResultActions resultActions = mockMvc.perform(
                post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        );

        // then
        // 서비스 로직에서 존재하지 않는 리소스 요청 시 보통 404 Not Found를 반환
        resultActions
                .andExpect(status().isNotFound())
                .andDo(print());
    }

}