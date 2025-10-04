package efub.assignment.community.member.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import efub.assignment.community.comment.repository.CommentRepository;
import efub.assignment.community.member.MemberFixture;
import efub.assignment.community.member.domain.Member;
import efub.assignment.community.member.dto.request.MemberRequestDto;
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
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class MemberControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("POST /members -> 201: 회원 생성 성공")
    void createMember() throws Exception {
        // given
        MemberRequestDto createMemberRequest = MemberFixture.MEMBER_CREATE_REQUEST();

        // when - then
       mockMvc.perform(post("/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createMemberRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.studentId").value(createMemberRequest.getStudentId()))
               .andExpect(jsonPath("$.university").value(createMemberRequest.getUniversity()))
               .andExpect(jsonPath("$.nickname").value(createMemberRequest.getNickname()))
               .andExpect(jsonPath("$.email").value(createMemberRequest.getEmail()));
    }

    @Test
    @DisplayName("POST /members -> 400 : 긴 닉네임으로 인한 회원 생성 실패")
    void createMember_tooLongNickName_Exception() throws Exception {
        // given
        MemberRequestDto tooLongNickNameRequest = MemberFixture.MEMBER_CREATE_REQUEST_NICKNAME_TOO_LONG();

        // when then
        mockMvc.perform(post("/members")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tooLongNickNameRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("닉네임은 1자 이상 8자 이하로 입력해주세요."))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.path").value("/members"));
    }

}