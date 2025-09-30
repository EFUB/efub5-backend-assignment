package efub.assignment.community.friend.service;


import efub.assignment.community.friend.domain.Friend;
import efub.assignment.community.friend.domain.FriendStatus;
import efub.assignment.community.friend.dto.FriendCreateRequestDto;
import efub.assignment.community.friend.dto.FriendCreateResponseDto;
import efub.assignment.community.friend.repository.FriendRepository;
import efub.assignment.community.member.entity.Member;
import efub.assignment.community.member.repository.MembersRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FriendService {
    private final FriendRepository friendRepository;
    private final MembersRepository memberRepository;

    // 친구 생성
    public FriendCreateResponseDto createFriend(@Valid FriendCreateRequestDto requestDto) {
        Member requestMember = findMember(requestDto.getRequesterId());
        Member targetMember = findMember(requestDto.getTargetId());
        if (friendRepository.findByMemberLowAndMemberHigh(requestMember, targetMember)){
            throw new IllegalArgumentException("이미 친구가 맺어져 있습니다.");
        }
        return FriendCreateResponseDto.from(friendRepository.save(requestDto.toEntity(requestMember, targetMember)));
    }

    // 회원 조회 함수
    private Member findMember(Long memberId){
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));
    }
}