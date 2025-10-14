package efub.assignment.community.post.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import efub.assignment.community.board.domain.Board;
import efub.assignment.community.board.repository.BoardRepository;
import efub.assignment.community.member.domain.Member;
import efub.assignment.community.member.repository.MemberRepository;
import efub.assignment.community.post.domain.Post;
import efub.assignment.community.post.dto.request.PostCreateRequestDto;
import efub.assignment.community.post.repository.PostRepository;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Transactional
class PostControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;
    @Autowired BoardRepository boardRepository;
    @Autowired MemberRepository memberRepository;
    @Autowired PostRepository postRepository;

    Long boardId;
    Long authorId;
    Long postId;

    @BeforeEach
    void setUp() {
        Member owner = Member.builder()
                .studentId("2176079").university("EWHA")
                .nickname("yerin").email("yerin@test.com")
                .password("Password@12345678")
                .build();
        memberRepository.save(owner);

        Board board = Board.builder()
                .owner(owner).name("free")
                .description("desc").notice(null)
                .build();
        boardRepository.save(board);
        boardId = board.getBoardId();

        Member author = Member.builder()
                .studentId("2176080").university("EWHA")
                .nickname("yerin2").email("yerin2@test.com")
                .password("Password@12345678")
                .build();
        memberRepository.save(author);
        authorId = author.getMemberId();

        Post post = Post.builder()
                .board(board).author(author)
                .anonymous(false).content("hello")
                .build();
        postRepository.save(post);
        postId = post.getPostId();
    }

    // POST /posts
    @Test
    @DisplayName("게시글 생성 - 성공(201)")
    void createPost_success() throws Exception {
        var req = new PostCreateRequestDto(boardId, true, authorId, "new content");

        mockMvc.perform(post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.boardId").value(boardId))
                .andExpect(jsonPath("$.anonymous").value(true))
                .andExpect(jsonPath("$.authorId").doesNotExist())
                .andExpect(jsonPath("$.content").value("new content"));
    }

    @Test
    @DisplayName("게시글 생성 - 실패: 존재하지 않는 boardId → 예외 던짐 검증 (해결 B)")
    void createPost_fail_boardNotFound() {
        var req = new PostCreateRequestDto(999999L, false, authorId, "x");

        assertThatThrownBy(() ->
                mockMvc.perform(post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
        )
                .isInstanceOf(ServletException.class)              // DispatcherServlet 레벨에서 터짐
                .hasCauseInstanceOf(RuntimeException.class)        // Service가 던진 예외
                .hasMessageContaining("Board not found");          // 메시지 확인
    }


    // PATCH /posts/{postId}
    @Test
    @DisplayName("게시글 수정 - 성공(200)")
    void updatePost_success() throws Exception {
        String body = "{\"content\":\"updated!\"}";

        mockMvc.perform(patch("/posts/{postId}", postId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.postId").value(postId))
                .andExpect(jsonPath("$.content").value("updated!"));
    }

    @Test
    @DisplayName("게시글 수정 - 실패(400): 없는 postId")
    void updatePost_fail_notFound() throws Exception {
        String body = "{\"content\":\"updated!\"}";

        mockMvc.perform(patch("/posts/{postId}", 987654321L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest()) // IllegalArgumentException → 400
                .andExpect(jsonPath("$.error").value("Illegal Argument"));
    }

    // GET /posts/{postId}
    @Test
    @DisplayName("게시글 상세 - 성공(200)")
    void getPost_success() throws Exception {
        mockMvc.perform(get("/posts/{postId}", postId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.postId").value(postId))
                .andExpect(jsonPath("$.boardId").value(boardId))
                .andExpect(jsonPath("$.anonymous").value(false))
                .andExpect(jsonPath("$.authorId").value(authorId));
    }

    @Test
    @DisplayName("게시글 상세 - 실패(400): 없는 postId")
    void getPost_fail_notFound() throws Exception {
        mockMvc.perform(get("/posts/{postId}", 999999L))
                .andExpect(status().isBadRequest()) // IllegalArgumentException → 400
                .andExpect(jsonPath("$.error").value("Illegal Argument"));
    }

    // GET /posts/{boardId}/list
    @Test
    @DisplayName("게시글 목록 - 성공(200)")
    void getPostList_success() throws Exception {
        mockMvc.perform(get("/posts/{boardId}/list", boardId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.boardId").value(boardId))
                .andExpect(jsonPath("$.count", greaterThanOrEqualTo(1)));
    }

    @Test
    @DisplayName("게시글 목록 - 실패(400): 없는 boardId")
    void getPostList_fail_boardNotFound() throws Exception {
        mockMvc.perform(get("/posts/{boardId}/list", 999999L))
                .andExpect(status().isBadRequest()) // IllegalArgumentException → 400
                .andExpect(jsonPath("$.error").value("Illegal Argument"));
    }

    // DELETE /posts/{postId}
    @Test
    @DisplayName("게시글 삭제 - 성공(200)")
    void deletePost_success() throws Exception {
        mockMvc.perform(delete("/posts/{postId}", postId))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("성공적으로 게시글 삭제가 완료되었습니다.")));
    }

    @Test
    @DisplayName("게시글 삭제 - 실패(구현의존)")
    void deletePost_fail_implDependent() throws Exception {
        mockMvc.perform(delete("/posts/{postId}", 999999L))
                // deleteByPostId 가 예외를 던지지 않으면 200
                .andExpect(status().isOk());
    }
}
