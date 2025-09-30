package efub.assignment.community.messageRooom.domain;

import efub.assignment.community.global.domain.BaseEntity;
import efub.assignment.community.member.domain.Member;
import efub.assignment.community.message.domain.Message;
import efub.assignment.community.post.domain.Post;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MessageRoom extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long messageRoomId;

    // 처음 보낸 사람 ID
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false)
    private Member sender;

    // 처음 받는 사람 ID
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "receiver_id", nullable = false)
    private Member receiver;

    // 쪽지가 시작된 게시글 ID
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    // 연관 메시지 리스트 (양방향 매핑)
    @OneToMany(mappedBy = "messageRoom", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Message> messages;

    // 생성일
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @Builder
    public MessageRoom(Member sender, Member receiver, Post post) {
        this.sender = sender;
        this.receiver = receiver;
        this.post = post;
    }

    public String getFirstMessageContent() {
        if (messages == null || messages.isEmpty()) {
            return null; // 메시지가 없는 경우
        }
        // 메시지를 createdAt 기준으로 정렬 후 첫 번째 메시지의 내용 반환
        return messages.stream()
                .sorted((m1, m2) -> m1.getCreatedAt().compareTo(m2.getCreatedAt()))
                .findFirst()
                .map(Message::getMessageContent)
                .orElse(null);
    }

}
