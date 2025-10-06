package com.efub_assignment.community.community.member.controller;

import com.efub_assignment.community.community.member.domain.Member;
import com.efub_assignment.community.community.member.dto.request.MemberRequestDto;
import com.efub_assignment.community.community.member.dto.response.MemberResponseDto;
import com.efub_assignment.community.community.member.repository.MemberRepository;
import com.efub_assignment.community.community.member.service.MemberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.mockito.BDDMockito.given;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
//@WebMvcTest(MemberController.class)
//@MockBean(JpaMetamodelMappingContext.class) // @EnableJpaAuditing 때문에 필요할 수 있으니 유지합니다.
class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MemberRepository memberRepository;

    private Member testMember;

//    @MockBean
//    private MemberService memberService;

    @BeforeEach
    void setUp(){
        testMember = Member.builder()
                .nickname("user")
                .email("user@email.com")
                .password("password")
                .school("school")
                .studentId("123456")
                .build();
        memberRepository.save(testMember);
    }

    @Test
    @DisplayName("멤버 생성 성공")
    void createMember_Success_test() throws Exception {
        //given
        MemberRequestDto request = new MemberRequestDto("email","password1","user1", "school1", "22");
        String requestBody = objectMapper.writeValueAsString(request);

        //when
        ResultActions resultActions = mockMvc.perform(
                post("/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        );

        //then
        resultActions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nickname").value("user1"))
                .andExpect(jsonPath("$.email").value("email"))
                .andDo(print());
    }

        @Test
        @DisplayName("멤버 생성(회원가입) 실패 - 중복된 이메일")
        void createMember_Fail_DuplicateEmail() throws Exception {
            // given
            MemberRequestDto requestDto = new MemberRequestDto("user@email.com", "anotherPassword!", "anotherUser", "Yonsei University", "3");
            String requestBody = objectMapper.writeValueAsString(requestDto);

            // when
            ResultActions resultActions = mockMvc.perform(
                    post("/members")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody)
            );

            // then
            resultActions
                    .andExpect(status().isBadRequest())
                    .andDo(print());
        }




//    @Test
//    @DisplayName("멤버 생성 성공")
//    void createMember_Success() throws Exception {
//        // given (준비)
//        // ✨ 2. DTO에 Builder가 없다면, 모든 필드를 받는 생성자가 있어야 합니다.
//        MemberRequestDto requestDto = new MemberRequestDto(
//                "test@example.com", "password123", "테스트유저", "이화여자대학교", "20241234"
//        );
//
//        // MemberResponseDto.from() 메서드가 Member 객체를 필요로 하므로, 임시 객체를 만들어줍니다.
//        Member tempMember = Member.builder()
//                .email(requestDto.getEmail()).nickname(requestDto.getNickname()).build();
//        MemberResponseDto responseDto = MemberResponseDto.from(tempMember);
//
//        given(memberService.createMember(any(MemberRequestDto.class)))
//                .willReturn(responseDto);
//
//        // when & then (실행 및 검증)
//        mockMvc.perform(post("/members") // ✨ 3. 올바른 post 메서드를 사용합니다.
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(requestDto)))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.nickname").value(requestDto.getNickname()))
//                .andExpect(jsonPath("$.email").value(requestDto.getEmail()));
//    }
}