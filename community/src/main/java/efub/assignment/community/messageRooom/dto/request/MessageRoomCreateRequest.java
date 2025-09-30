package efub.assignment.community.messageRooom.dto.request;

import efub.assignment.community.member.domain.Member;
import efub.assignment.community.messageRooom.domain.MessageRoom;
import efub.assignment.community.post.domain.Post;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record MessageRoomCreateRequest(
        @NotNull Long senderId,
        @NotNull Long receiverId,
        @NotBlank(message = "내용을 입력해야 합니다.") String messageContent,
        @NotNull Long postId
) {
    public MessageRoom toEntity(Member sender, Member receiver, Post post) {
        return MessageRoom.builder()
                .sender(sender)
                .receiver(receiver)
                .post(post)
                .build();
    }
}
