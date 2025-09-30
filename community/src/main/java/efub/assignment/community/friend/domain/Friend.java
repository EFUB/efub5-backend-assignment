package efub.assignment.community.friend.domain;


import efub.assignment.community.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Builder
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Getter
@Table(name = "friends")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Friend{
    // ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long friendId;

    // (minId, maxId) 보관 → (A,B)와 (B,A) 중복 방지
    @ManyToOne(optional = false)
    @JoinColumn(name = "memberLow")
    private Member memberLow;

    @ManyToOne(optional = false)
    @JoinColumn(name = "memberHigh")
    private Member memberHigh;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private FriendStatus status; // PENDING, ACCEPTED

    @ManyToOne(optional = false)
    @JoinColumn(name = "requestedBy")
    private Member requestedBy;

    private LocalDateTime requestedAt;

    @Builder
    public Friend(Member memberLow, Member memberHigh, Member requestedBy){
        if(memberLow.getMemberId() < memberHigh.getMemberId()){
            this.memberLow = memberLow;
            this.memberHigh = memberHigh;
        }
        else{
            this.memberLow = memberHigh;
            this.memberHigh = memberLow;
        }
        this.status = FriendStatus.Pending;
        this.requestedBy = requestedBy;
        this.requestedAt = LocalDateTime.now();
    }

    // 친구 요청 수락 상태 변경
    public void changeStatus(){this.status = FriendStatus.Accepted;}

}