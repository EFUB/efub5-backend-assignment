package efub.assignment.community.Friend;


import efub.assignment.community.friend.domain.Friend;
import efub.assignment.community.friend.domain.FriendStatus;
import efub.assignment.community.member.entity.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class FriendTest {
    private Friend friend;
    Member memberLow = member(1L, "01", "nick1", "a@test.com");
    Member memberHigh = member(2L, "02", "nick2", "b@test.com");

    @BeforeEach
    void setFriend(){
        friend = Friend.builder()
                .memberLow(memberLow)
                .memberHigh(memberHigh)
                .status(FriendStatus.Pending)
                .requestedBy(memberLow)
                .build();
    }

    @Test
    void 친구_생성_정상작동(){
        assertNotNull(friend);
        assertEquals(memberLow, friend.getMemberLow());
        assertEquals(memberHigh, friend.getMemberHigh());
        assertEquals(FriendStatus.Pending, friend.getStatus());
        assertEquals(memberLow, friend.getRequestedBy());
    }

    @Test
    void 친구_요청_수락으로_변경(){
        friend.changeStatus();
        assertEquals(FriendStatus.Accepted, friend.getStatus());
    }

    // Reflection > MemberId 생성
    private Member member(Long id, String studentId, String nickname, String email) {
        Member member = new Member(studentId, "univ", nickname, email, "pw");
        try {
            Field f = Member.class.getDeclaredField("memberId");
            f.setAccessible(true);
            f.set(member, id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return member;
    }
}