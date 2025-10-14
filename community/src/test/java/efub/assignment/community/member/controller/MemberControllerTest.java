package efub.assignment.community.member.controller;

import efub.assignment.community.member.domain.Member;
import efub.assignment.community.member.repository.MemberRepository;
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

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class MemberControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    MemberRepository memberRepository;

    private Long savedMemberId;

    @BeforeEach
    void seed() {
        Member member = new Member("yepot@email.com", "testpw", "yepot", "ewha", 2371040L);
        savedMemberId = memberRepository.save(member).getMemberId();
    }

    @Test
    @DisplayName("GET /members/{memberId} → 멤버 조회 성공 시 200 OK와 JSON 반환")
    void getMember_success() throws Exception {
        mockMvc.perform(
                        get("/members/{memberId}", savedMemberId)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.memberId").value(Math.toIntExact(savedMemberId)))
                .andExpect(jsonPath("$.nickname").value("yepot"))
                .andExpect(jsonPath("$.email").value("yepot@email.com"))
                .andExpect(jsonPath("$.studentId").value(2371040))
                .andExpect(jsonPath("$.university").value("ewha"));
    }

    @Test
    @DisplayName("GET /members/{memberId} → 존재하지 않는 멤버면 500 & IllegalArgumentException (현재 동작 기준)")
    void getMember_notFound_returns500_illegalArgument() throws Exception {
        Long nonExistingId = (savedMemberId == null ? 999999L : savedMemberId + 9999);

        mockMvc.perform(get("/members/{memberId}", nonExistingId)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(result -> {Throwable ex = result.getResolvedException();
                   assertThat(ex).isInstanceOf(IllegalArgumentException.class);
                   assertThat(ex.getMessage()).contains("해당 멤버를 찾을 수 없습니다.");
                });
    }




}