package com.efub.community.member.service;

import com.efub.community.member.domain.entity.Member;
import com.efub.community.member.dto.CreateMemberRequestDto;
import com.efub.community.member.dto.CreateMemberResponseDto;
import com.efub.community.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    private MemberRepository membersRepository;

    @InjectMocks
    private MemberService memberService;

    @Test
    @DisplayName("회원가입_성공_테스트")
    void 회원_생성_성공() {
        // given
        CreateMemberRequestDto requestDto = mock(CreateMemberRequestDto.class);
        given(requestDto.getEmail()).willReturn("test@example.com");

        Member toSave = Member.builder()
                .email("test@example.com")
                .password("password")
                .nickname("nick")
                .university("uni")
                .studentId("20250001")
                .build();

        given(requestDto.toEntity()).willReturn(toSave);
        given(membersRepository.existsByEmail("test@example.com")).willReturn(false);

        // save 시에 id가 세팅되었다고 시뮬레이션
        given(membersRepository.save(any(Member.class))).willAnswer(invocation -> {
            Member arg = invocation.getArgument(0);
            ReflectionTestUtils.setField(arg, "memberId", 1L);
            return arg;
        });

        // when
        CreateMemberResponseDto responseDto = memberService.createMember(requestDto);

        // then
        assertThat(responseDto.getEmail()).isEqualTo("test@example.com");
        assertThat(responseDto.getId()).isEqualTo(1L);
        then(membersRepository).should(times(1)).existsByEmail("test@example.com");
        then(membersRepository).should(times(1)).save(any(Member.class));
    }
}