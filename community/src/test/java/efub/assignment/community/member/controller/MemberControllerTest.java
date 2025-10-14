package efub.assignment.community.member.controller;

import efub.assignment.community.member.entity.Member;
import efub.assignment.community.member.repository.MembersRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class MemberControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired MembersRepository membersRepository;

    @BeforeEach
    void seed() {
        Member member = new Member("test@example.com", "testpw", "efub", "test대학교", "2323");
        membersRepository.save(member);
    }

    @Test
    @DisplayName("GET /members/{memberId} > 200 & 응답 필드 검증")
    void getMember_200() throws Exception {
        // given
        Member member = membersRepository.findAll().get(0);

        // when then
        mockMvc.perform(get("/members/{memberId}", member.getMemberId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(member.getMemberId()))
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.nickname").value("efub"))
                .andExpect(jsonPath("$.university").value("test대학교"))
                .andExpect(jsonPath("$.studentId").value("2323"));
    }

    @Test
    @DisplayName("GET /members/{memberId} > 400 & 존재하지 않는 회원 조회")
    void getMember_400() throws Exception {
        // given - 존재하지 않는 회원 ID
        Long nonExistentMemberId = 999L;

        // when then
        mockMvc.perform(get("/members/{memberId}", nonExistentMemberId))
                .andExpect(status().isBadRequest());
    }
}