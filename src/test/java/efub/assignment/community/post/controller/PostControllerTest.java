package efub.assignment.community.post.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import efub.assignment.community.board.domain.Board;
import efub.assignment.community.board.repository.BoardRepository;
import efub.assignment.community.member.MemberFixture;
import efub.assignment.community.member.domain.Member;
import efub.assignment.community.member.dto.request.MemberRequestDto;
import efub.assignment.community.member.repository.MemberRepository;
import efub.assignment.community.post.PostFixture;
import efub.assignment.community.post.dto.request.PostCreateRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class PostControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    BoardRepository boardRepository;
    @Autowired
    MemberRepository memberRepository;

    Member createdMember;
    Board createdBoard;

    @BeforeEach
    void setUp() {
        // 초기 멤버 저장
        MemberRequestDto createdMemberRequest = MemberFixture.MEMBER_CREATE_REQUEST();
        createdMember = createdMemberRequest.toEntity();
        memberRepository.save(createdMember);

        // 초기 게시판 저장
        createdBoard = new Board(createdMember, "게시판 설명", "게시판 공지", "게시판 이름");
        boardRepository.save(createdBoard);
    }

    @DisplayName("POST /posts -> 201 : 게시물 생성 성공")
    @Test
    void createPost() throws Exception {
        // given
        PostCreateRequestDto createPostRequest = PostFixture.POST_CREATE_REQUEST(createdBoard.getBoardId(), createdMember.getMemberId());

        // when then
        mockMvc.perform(post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createPostRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.authorId").value(createPostRequest.authorId()))
                .andExpect(jsonPath("$.anonymous").value(createPostRequest.anonymous()))
                .andExpect(jsonPath("$.content").value(createPostRequest.content()))
                .andExpect(jsonPath("$.createdAt").exists())
                .andExpect(jsonPath("$.updatedAt").exists());
    }

    @DisplayName("POST /posts -> 400 : 빈 게시물 내용으로 인한 게시물 생성 실패")
    @Test
    void createPost_blankContent_Exception() throws Exception {
        // given
        PostCreateRequestDto createPostRequest = PostFixture.POST_CREATE_REQUEST_BLANK_CONTENT(createdBoard.getBoardId(), createdMember.getMemberId());

        // when - then
        mockMvc.perform(post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createPostRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("게시물 내용을 입력해주세요."))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.path").value("/posts"));
    }
}