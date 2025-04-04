package com.efub_assignment.community.community.member.service;

import com.efub_assignment.community.community.member.domain.Member;
import com.efub_assignment.community.community.member.domain.MemberStatus;
import com.efub_assignment.community.community.member.dto.MemberRequestDto;
import com.efub_assignment.community.community.member.dto.MemberResponseDto;
import com.efub_assignment.community.community.member.repository.MemberRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;

    // 멤버 생성
    public MemberResponseDto createMember(MemberRequestDto requestDto){
    if(memberRepository.existsByEmail(requestDto.getEmail())){
        throw new IllegalArgumentException("이미 존재하는 이메일 입니다.");
    }
    if(memberRepository.existsByStudentId(requestDto.getStudentId())){
        throw new IllegalArgumentException(("이미 존재하는 학번입니다."));
    }

    Member member = requestDto.toEntity();
    Member savedMember = memberRepository.save(member);

    return MemberResponseDto.from(savedMember);
    }

    // 닉네임 수정
    public MemberResponseDto updateMember(Long memberId,MemberRequestDto requestDto){
        Member member = memberRepository.findByMemberId(memberId)
                .orElseThrow(()->new IllegalArgumentException(("해당 계정을 찾을 수 없습니다.")));
        member.updateNickname(requestDto.getNickname());
        return MemberResponseDto.from(member);

    }

    // 멤버 1명 조회
    @Transactional(readOnly=true)
    public MemberResponseDto getMember(Long memberId){
        Member member =memberRepository.findByMemberId(memberId)
                .orElseThrow(()-> new IllegalArgumentException("해당 계정을 찾을 수 없습니다."));
        return MemberResponseDto.from(member);
    }

    // 멤버 논리적 삭제
    public void deleteMember(Long memberId){
        Member member = memberRepository.findByMemberId(memberId)
                .orElseThrow(()-> new IllegalArgumentException("해당 계정을 찾을 수 없습니다."));
        member.changeStatus(MemberStatus.UNRESISTER);
        memberRepository.save(member);
    }
}
