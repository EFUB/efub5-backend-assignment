package efub.assignment.community.member.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import efub.assignment.community.board.domain.Board;
import efub.assignment.community.board.repository.BoardRepository;
import efub.assignment.community.comment.domain.Comment;
import efub.assignment.community.comment.repository.CommentRepository;
import efub.assignment.community.member.domain.Member;
import efub.assignment.community.member.repository.MemberRepository;
import efub.assignment.community.post.domain.Post;
import efub.assignment.community.post.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete; // (미사용 시 제거 가능)
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Transactional
class MemberControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;

    @Autowired MemberRepository memberRepository;
    @Autowired BoardRepository boardRepository;
    @Autowired PostRepository postRepository;
    @Autowired CommentRepository commentRepository;

    private Long memberId;

    @BeforeEach
    void setUp() {

        Member m = Member.builder()
                .studentId("2176079")
                .university("EFUB Univ")
                .nickname("yerin")
                .email("yerin@test.com")
                .password("ValidPass@1234567")
                .build();
        memberRepository.save(m);
        memberId = m.getMemberId();

        Member owner = Member.builder()
                .studentId("2176080")
                .university("EFUB Univ")
                .nickname("gimye")
                .email("gimye@test.com")
                .password("ValidPass@1234567")
                .build();
        memberRepository.save(owner);

        Board b = Board.builder()
                .owner(owner)
                .name("free")
                .description("desc")
                .notice(null)
                .build();
        boardRepository.save(b);

        Post p = Post.builder()
                .board(b)
                .author(m)
                .anonymous(false)
                .content("content")
                .build();
        postRepository.save(p);

        Comment c = Comment.builder()
                .post(p)
                .commenter(m)
                .content("nice!")
                .build();
        commentRepository.save(c);
    }

    // POST /members (create)

    @Test
    @DisplayName("회원 생성 - 성공(201)")
    void createMember_success() throws Exception {
        // 새로운 사용자: yerin2
        String body = """
        {
          "studentId":"2176081",
          "university":"EFUB Univ",
          "nickname":"yerin2",
          "email":"yerin2@test.com",
          "password":"ValidPass@1234567"
        }
        """;

        mockMvc.perform(post("/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.studentId").value("2176081"))
                .andExpect(jsonPath("$.university").value("EFUB Univ"))
                .andExpect(jsonPath("$.nickname").value("yerin2"))
                .andExpect(jsonPath("$.email").value("yerin2@test.com"));
    }

    @Test
    @DisplayName("회원 생성 - 실패(400): 이메일 중복")
    void createMember_fail_duplicateEmail() throws Exception {
        // setUp에서 이미 yerin@test.com 사용
        String body = """
        {
          "studentId":"2176082",
          "university":"EFUB Univ",
          "nickname":"gimye2",
          "email":"yerin@test.com",
          "password":"ValidPass@1234567"
        }
        """;

        mockMvc.perform(post("/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Illegal Argument"))
                .andExpect(jsonPath("$.message", notNullValue()));
    }

    @Test
    @DisplayName("회원 생성 - 실패(400): DTO 유효성 위반(비밀번호 규칙)")
    void createMember_fail_validation() throws Exception {
        String body = """
        {
          "studentId":"2176083",
          "university":"EFUB Univ",
          "nickname":"yerin3",
          "email":"yerin3@test.com",
          "password":"short"
        }
        """;

        mockMvc.perform(post("/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message", containsString("비밀번호")));
    }

    // GET /members/{memberId} (detail)

    @Test
    @DisplayName("회원 조회 - 성공(200)")
    void getMember_success() throws Exception {
        mockMvc.perform(get("/members/{memberId}", memberId))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.studentId").value("2176079"))
                .andExpect(jsonPath("$.email").value("yerin@test.com"));
    }

    @Test
    @DisplayName("회원 조회 - 실패(400): 없는 회원")
    void getMember_fail_notFound() throws Exception {
        mockMvc.perform(get("/members/{memberId}", 999999L))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Illegal Argument"))
                .andExpect(jsonPath("$.message", notNullValue()));
    }

    // PATCH /members/profile/{memberId}

    @Test
    @DisplayName("닉네임 수정 - 성공(200)")
    void updateNickname_success() throws Exception {
        String body = """
        {
          "studentId":"2176079",
          "university":"EFUB Univ",
          "nickname":"yerin-up",
          "email":"yerin@test.com",
          "password":"ValidPass@1234567"
        }
        """;

        mockMvc.perform(patch("/members/profile/{memberId}", memberId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nickname").value("yerin-up"));
    }

    @Test
    @DisplayName("닉네임 수정 - 실패(400): 없는 회원")
    void updateNickname_fail_notFound() throws Exception {
        String body = """
        {
          "studentId":"2176079",
          "university":"EFUB Univ",
          "nickname":"any",
          "email":"yerin@test.com",
          "password":"ValidPass@1234567"
        }
        """;

        mockMvc.perform(patch("/members/profile/{memberId}", 999999L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Illegal Argument"))
                .andExpect(jsonPath("$.message", notNullValue()));
    }

    // PATCH /members/{memberId} (delete: 상태 변경)

    @Test
    @DisplayName("회원 탈퇴(상태 변경) - 성공(200)")
    void deleteMember_success() throws Exception {
        mockMvc.perform(patch("/members/{memberId}", memberId))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("성공적으로 탈퇴가 완료되었습니다.")));
    }

    @Test
    @DisplayName("회원 탈퇴(상태 변경) - 실패(400): 없는 회원")
    void deleteMember_fail_notFound() throws Exception {
        mockMvc.perform(patch("/members/{memberId}", 999999L))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Illegal Argument"))
                .andExpect(jsonPath("$.message", notNullValue()));
    }

    // GET /members/{memberId}/comments (작성자별 댓글)

    @Test
    @DisplayName("작성자별 댓글 조회 - 성공(200)")
    void getMemberComments_success() throws Exception {
        mockMvc.perform(get("/members/{memberId}/comments", memberId))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.memberId").value(memberId));
    }

    @Test
    @DisplayName("작성자별 댓글 조회 - 실패(구현의존): 없는 회원(현 구현상 200 가능)")
    void getMemberComments_fail_implDependent() throws Exception {
        mockMvc.perform(get("/members/{memberId}/comments", 999999L))
                .andExpect(status().isOk());
    }
}
