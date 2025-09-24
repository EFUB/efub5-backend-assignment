package com.efub.community.member.service;

import com.efub.community.global.exception.BlogException;
import com.efub.community.global.exception.ExceptionCode;
import com.efub.community.member.domain.entity.Member;
import com.efub.community.member.domain.entity.MemberStatus;
import com.efub.community.member.dto.CreateMemberRequestDto;
import com.efub.community.member.dto.CreateMemberResponseDto;
import com.efub.community.member.dto.MemberResponseDto;
import com.efub.community.member.dto.MemberUpdateRequestDto;
import com.efub.community.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor // 한 줄로 받아오게..?
@Transactional
public class MemberService {

    private final MemberRepository membersRepository;

    // 회원 조회
    @Transactional(readOnly = true)
    public MemberResponseDto getMember(Long memberId) {
        // 존재여부 확인
        Member member = membersRepository.findByMemberId(memberId).orElseThrow( ()-> new IllegalArgumentException("해당 회원을 찾을 수 없습니다."));
        return MemberResponseDto.from(member);
    }

    // 회원가입
    public CreateMemberResponseDto createMember(CreateMemberRequestDto requestDto) {
        if (membersRepository.existsByEmail(requestDto.getEmail())) {
            throw new BlogException(ExceptionCode.DUPLICATE_EMAIL);
        }
        Member member = requestDto.toEntity();
        Member saved = membersRepository.save(member);

        return CreateMemberResponseDto.from(saved);
    }

    // 프로필(자기소개) 수정
    public MemberResponseDto updateMember(Long memberId, MemberUpdateRequestDto requestDto) {
        Member member = membersRepository.findByMemberId(memberId).orElseThrow(()-> new IllegalArgumentException("해당 회원을 찾을 수 없습니다."));
        member.updateMember(requestDto.getEmail(), requestDto.getNickname(), requestDto.getUniversity(), requestDto.getStudentId());
        return MemberResponseDto.from(member);
    }

    // 회원 논리적 삭제 (status 변경)
    public void deleteMember(Long memberId) {
        Member member = membersRepository.findByMemberId(memberId).orElseThrow(()-> new IllegalArgumentException("해당 회원을 찾을 수 없습니다."));
        member.changeStatus(MemberStatus.DEACTIVATED);
    }
}