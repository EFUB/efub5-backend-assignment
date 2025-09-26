package com.efub_assignment.community.community.member.service;

import com.efub_assignment.community.community.member.domain.Member;
import com.efub_assignment.community.community.member.dto.request.MemberRequestDto;
import com.efub_assignment.community.community.member.dto.response.MemberResponseDto;
import com.efub_assignment.community.community.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

// ✨ 1. import 문을 수정합니다.
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify; // ✨ 올바른 verify를 import 합니다.

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @InjectMocks
    private MemberService memberService;

    @Mock
    private MemberRepository memberRepository;

    @Test
    @DisplayName("멤버 생성 성공")
    void createMember_Success() {
        // given (준비)
        MemberRequestDto requestDto = new MemberRequestDto(
                "test@example.com", "pw123", "테스트유저", "이화여대", "240001"
        );
        Member member = requestDto.toEntity();

        // Mock 객체 행동 정의
        given(memberRepository.existsByEmail(anyString())).willReturn(false);
        given(memberRepository.existsByStudentId(anyString())).willReturn(false);
        given(memberRepository.save(any(Member.class))).willReturn(member);

        // when (실행)
        MemberResponseDto responseDto = memberService.createMember(requestDto);

        // then (검증)
        assertThat(responseDto.getEmail()).isEqualTo(requestDto.getEmail());
        assertThat(responseDto.getNickname()).isEqualTo(requestDto.getNickname());

        // ✨ 2. 이제 verify 메서드에서 오류가 발생하지 않습니다.
        verify(memberRepository, times(1)).save(any(Member.class));
    }
}