package efub.assignment.community.friend.dto;

import efub.assignment.community.friend.domain.Friend;

import efub.assignment.community.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor   // JSON 바인딩용
@AllArgsConstructor  // 테스트에서 편하게 new 생성자 쓸 수 있게
public class FriendCreateRequestDto {
    private Long requesterId;  // 요청자 ID
    private Long targetId;     // 친구 대상 ID

    public Friend toEntity(Member requester, Member target) {
        return Friend.builder()
                .memberLow(requester)
                .memberHigh(target)
                .requestedBy(requester)
                .build();
    }

}