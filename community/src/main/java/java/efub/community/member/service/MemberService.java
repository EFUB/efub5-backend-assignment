package java.efub.community.member.service;

import java.efub.community.member.domain.entity.Member;
import java.efub.community.member.domain.entity.MemberStatus;
import java.efub.community.member.dto.CreateMemberRequestDto;
import java.efub.community.member.dto.CreateMemberResponseDto;
import java.efub.community.member.dto.MemberResponseDto;
import java.efub.community.member.dto.MemberUpdateRequestDto;
import java.efub.community.member.repository.MemberRepository;
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
        if(membersRepository.existsByEmail(requestDto.getEmail())) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다." + requestDto.getEmail());
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