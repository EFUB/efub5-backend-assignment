package efub.assignment.community.friend.dto;

import efub.assignment.community.friend.domain.Friend;
import efub.assignment.community.friend.domain.FriendStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FriendCreateResponseDto {
    private Long friendId;
    private FriendStatus status;      // Pending

    public static FriendCreateResponseDto from(Friend friend) {
        return FriendCreateResponseDto.builder()
                .friendId(friend.getFriendId())
                .status(friend.getStatus())
                .build();
    }
}