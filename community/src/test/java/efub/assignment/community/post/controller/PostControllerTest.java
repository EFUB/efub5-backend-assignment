package efub.assignment.community.post.controller;

import efub.assignment.community.board.domain.Board;
import efub.assignment.community.board.repository.BoardRepository;
import efub.assignment.community.member.domain.Member;
import efub.assignment.community.member.repository.MemberRepository;
import efub.assignment.community.post.domain.Post;
import efub.assignment.community.post.repository.PostRepository;
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

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class PostControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    MemberRepository accountsRepository;
    @Autowired
    PostRepository postRepository;
    @Autowired
    BoardRepository boardRepository;

    private Member writer;
    private Board savedBoard;
    private Post post;

    @BeforeEach
    void seed() throws Exception {
        writer = accountsRepository.save(new Member("yepot@email.com", "testpw", "yepot", "ewha", 2371040L));
        savedBoard = boardRepository.save(Board.builder()
                        .boardName("boardName")
                        .description("자유 게시판")
                        .notice("공지입니다")
                        .owner(writer)
                        .build());
        Post tmp = Post.builder()
                .writer(writer)
                .content("원본 내용은 다섯글자 이상")
                .anonymous(false)
                .build();

        Field f = Post.class.getDeclaredField("board");
        f.setAccessible(true);
        f.set(tmp, savedBoard);

        post = postRepository.save(tmp);
    }

    @Test
    @DisplayName("PATCH /posts/{postId} → 204 No Content & DB 내용 변경 확인 (내용수정 성공)")
    void updatePost_success_204() throws Exception {
        // given
        String body = """
            {"content":"수정된 내용은 다섯글자 이상"}
            """;

        // when then
        mockMvc.perform(patch("/posts/{postId}", post.getId())
                                .header("Auth-Id", writer.getMemberId())
                                .header("Auth-Password", writer.getPassword())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body))
                .andExpect(status().isNoContent());

        Post updated = postRepository.findById(post.getId()).orElseThrow();
        assertEquals("수정된 내용은 다섯글자 이상", updated.getContent());
    }

    @Test
    @DisplayName("DELETE /posts/{postId} → 비밀번호 불일치로 삭제 실패 (POST_ACCOUNT_MISMATCH)")
    void deletePost_fail_accountMismatch() throws Exception {

        // when then
        mockMvc.perform(delete("/posts/{postId}", post.getId())
                                .header("Auth-Id", writer.getMemberId())
                                .header("Auth-Password", "wrong-password"))
                .andExpect(status().isUnauthorized());

        assertTrue(postRepository.findById(post.getId()).isPresent());
    }
}
