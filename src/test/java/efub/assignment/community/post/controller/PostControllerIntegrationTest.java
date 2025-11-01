package efub.assignment.community.post.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import efub.assignment.community.board.domain.Board;
import efub.assignment.community.board.repository.BoardRepository;
import efub.assignment.community.member.domain.Member;
import efub.assignment.community.member.dto.request.MemberRequestDto;
import efub.assignment.community.member.repository.MemberRepository;
import efub.assignment.community.post.dto.request.PostCreateRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class PostControllerIntegrationTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    BoardRepository boardRepository;
    @Autowired
    MemberRepository memberRepository;

    @BeforeEach
    void setUp() {
        Member member = Member.builder()
                .studentId("2271000")
                .university("ewha")
                .email("jettie@ewha.ac.kr")
                .nickname("제티")
                .password("1234567abcdefg!!")
                .build();
        memberRepository.save(member);

        Board board = Board.builder()
                .owner(member)
                .name("게시판")
                .description("설명어쩌구저쩌구")
                .build();
        boardRepository.save(board);
    }

    @Test
    @DisplayName("POST /posts -> 201, 게시글 생성 성공")
    void createPost_and_persist() throws Exception {
        // given
        PostCreateRequestDto requestDto = new PostCreateRequestDto(1L, false, 1L, "어쩌구우하하");

        // when & then
        MvcResult res = mockMvc.perform(post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.boardId").value(requestDto.boardId()))
                .andExpect(jsonPath("$.anonymous").value(requestDto.anonymous()))
                .andExpect(jsonPath("$.authorId").value(requestDto.authorId()))
                .andExpect(jsonPath("$.content").value(requestDto.content()))
                .andReturn();
    }

    @Test
    @DisplayName("POST /posts -> 400, 빈 내용의 게시물 입력한 경우")
    void createPost_content_Empty() throws Exception {
        // given
        PostCreateRequestDto requestDto = new PostCreateRequestDto(1L, false, 1L, "");

        // when & then
        MvcResult res = mockMvc.perform(post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("내용을 입력해주세요."))
                .andExpect(jsonPath("$.path").value("/posts"))
                .andReturn();
    }



}
