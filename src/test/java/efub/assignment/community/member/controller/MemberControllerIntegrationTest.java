package efub.assignment.community.member.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import efub.assignment.community.member.dto.request.MemberRequestDto;
import efub.assignment.community.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
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
class MemberControllerIntegrationTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("POST /members -> 201, 회원 생성 성공")
    void createMember_and_persist() throws Exception {
        // given
        MemberRequestDto requestDto = MemberRequestDto.builder()
                .studentId("2272000")
                .university("ewha")
                .email("jettie@ewha.ac.kr")
                .nickname("제티")
                .password("1234567abcdefg!!")
                .build();

        // when & then
        MvcResult res = mockMvc.perform(post("/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.studentId").value(requestDto.getStudentId()))
                .andExpect(jsonPath("$.university").value(requestDto.getUniversity()))
                .andExpect(jsonPath("$.nickname").value(requestDto.getNickname()))
                .andExpect(jsonPath("$.email").value(requestDto.getEmail()))
                .andReturn();
    }

    @Test
    @DisplayName("POST /members -> 400, 비밀번호가 입력 형식을 맞추지 않을 때")
    void createMember_PasswordNotValid() throws Exception {
        // given
        MemberRequestDto requestDto = MemberRequestDto.builder()
                .studentId("2272000")
                .university("ewha")
                .email("jettie@ewha.ac.kr")
                .nickname("제티")
                .password("1234")  //비밀번호 형식 invalid
                .build();

        // when & then
        MvcResult res = mockMvc.perform(post("/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("비밀번호는 16자 이상이며, 영문, 숫자, 특수문자를 포함해야 합니다."))
                .andExpect(jsonPath("$.path").value("/members"))
                .andReturn();
    }


}