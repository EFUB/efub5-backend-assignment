package efub.assignment.community.member.service;

import efub.assignment.community.member.domain.Member;
import efub.assignment.community.member.domain.MemberStatus;
import efub.assignment.community.member.dto.CreateMemberRequestDto;
import efub.assignment.community.member.dto.CreateMemberResponseDto;
import efub.assignment.community.member.dto.MemberResponseDto;
import efub.assignment.community.member.dto.UpdateMemberRequestDto;
import efub.assignment.community.member.repository.MembersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MembersService {

    private final MembersRepository membersRepository;

    //멤버 단건 조회: GET member/{memberId}
    @Transactional(readOnly=true)
    public MemberResponseDto getMember(Long memberId){
        Member member=membersRepository.findByMemberId(memberId)
                .orElseThrow(()->new IllegalArgumentException("해당 멤버를 찾을 수 없습니다."));
        return MemberResponseDto.from(member);
    }

    //멤버 생성
    @Transactional
    public CreateMemberResponseDto createMember(CreateMemberRequestDto requestDto){

        // 이메일 형식 유효성 검사
        if (!isValidEmail(requestDto.getEmail())) {
            throw new IllegalArgumentException("유효하지 않은 이메일 형식입니다: " + requestDto.getEmail());
        }
        Member member=requestDto.toEntity();
        Member savedMember=membersRepository.save(member);

        return CreateMemberResponseDto.from(savedMember);
    }

    private boolean isValidEmail(String email) {
        // 간단한 이메일 정규식
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return email != null && email.matches(emailRegex);
    }

    //멤버 수정
    @Transactional
    public MemberResponseDto updateMember(Long memberId, UpdateMemberRequestDto requestDto){
        Member member=membersRepository.findByMemberId(memberId)
                .orElseThrow(()->new IllegalArgumentException("해당 회원을 찾을 수 없습니다."));
        member.updateMember(requestDto.getEmail(), requestDto.getPassword(), requestDto.getNickname(), requestDto.getUniversity(), requestDto.getStudentId());
        Member updatedMember=membersRepository.save(member);
        return MemberResponseDto.from(updatedMember);
    }

    //멤버 논리적 삭제 (status 변경)
    @Transactional
    public void deleteMember(Long memberId){
        Member member=membersRepository.findByMemberId(memberId)
                .orElseThrow(()->new IllegalArgumentException("삭제할 유저를 찾을 수 없습니다."));
        member.changeStatus(MemberStatus.DEACTIVATED);
        membersRepository.save(member);
    }

}


