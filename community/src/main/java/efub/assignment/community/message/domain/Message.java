package efub.assignment.community.message.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import efub.assignment.community.global.domain.BaseEntity;
import efub.assignment.community.member.domain.Member;
import efub.assignment.community.messageRooom.domain.MessageRoom;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Message extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long messageId;

    // 쪽지 보낸 사람 ID
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false)
    private Member sender;

//    // 쪽지 받은 사람 ID
//    @ManyToOne(fetch=FetchType.LAZY)
//    @JoinColumn(name = "receiver_id", nullable = false)
//    @JsonIgnore
//    private Member receiver;

    // 쪽지방 ID
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "messageRoom_id", nullable = false)
    private MessageRoom messageRoom;

    // 쪽지 내용
    @Column(nullable = false)
    private String messageContent;

    // 쪽지 보낸 시각
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    // 보낸 건지 받은 건지 여부
    private boolean isSentByMe;

    // 내용 수정
    public void edit(String newContent) {
        if (newContent == null || newContent.isBlank()) {
            throw new IllegalArgumentException("메시지 내용은 비어 있을 수 없습니다.");
        }
        this.messageContent = newContent;
    }

    @Builder
    public Message(Member sender, MessageRoom messageRoom, String messageContent, boolean isSentByMe){
        this.sender=sender;
        this.messageRoom=messageRoom;
        this.messageContent=messageContent;
        this.isSentByMe=isSentByMe;
    }

}
