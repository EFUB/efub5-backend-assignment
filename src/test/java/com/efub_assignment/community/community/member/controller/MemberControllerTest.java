package com.efub_assignment.community.community.member.controller;

import com.efub_assignment.community.community.member.domain.Member;
import com.efub_assignment.community.community.member.dto.request.MemberRequestDto;
import com.efub_assignment.community.community.member.dto.response.MemberResponseDto;
import com.efub_assignment.community.community.member.service.MemberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.mockito.BDDMockito.given;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MemberController.class)
@MockBean(JpaMetamodelMappingContext.class) // @EnableJpaAuditing 때문에 필요할 수 있으니 유지합니다.
class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MemberService memberService;

    @Test
    @DisplayName("멤버 생성 성공")
    void createMember_Success() throws Exception {
        // given (준비)
        // ✨ 2. DTO에 Builder가 없다면, 모든 필드를 받는 생성자가 있어야 합니다.
        MemberRequestDto requestDto = new MemberRequestDto(
                "test@example.com", "password123", "테스트유저", "이화여자대학교", "20241234"
        );

        // MemberResponseDto.from() 메서드가 Member 객체를 필요로 하므로, 임시 객체를 만들어줍니다.
        Member tempMember = Member.builder()
                .email(requestDto.getEmail()).nickname(requestDto.getNickname()).build();
        MemberResponseDto responseDto = MemberResponseDto.from(tempMember);

        given(memberService.createMember(any(MemberRequestDto.class)))
                .willReturn(responseDto);

        // when & then (실행 및 검증)
        mockMvc.perform(post("/members") // ✨ 3. 올바른 post 메서드를 사용합니다.
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nickname").value(requestDto.getNickname()))
                .andExpect(jsonPath("$.email").value(requestDto.getEmail()));
    }
}