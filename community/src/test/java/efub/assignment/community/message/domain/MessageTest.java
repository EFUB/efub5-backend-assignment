package efub.assignment.community.message.domain;

import efub.assignment.community.member.domain.Member;
import efub.assignment.community.messageRooom.domain.MessageRoom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class MessageTest {

    private Message message;

    @BeforeEach
    void setUp() {
        Member sender = Mockito.mock(Member.class);
        MessageRoom room = Mockito.mock(MessageRoom.class);

        message = Message.builder()
                .sender(sender)
                .messageRoom(room)
                .messageContent("안녕")
                .isSentByMe(true)
                .build();
    }

    @Test
    void 내용수정_정상동작() {
        // when
        message.edit("수정된 내용");

        // then
        assertEquals("수정된 내용", message.getMessageContent());
    }

    @Test
    void 내용이_공백이면_수정실패() {
        assertThrows(IllegalArgumentException.class, () -> message.edit("  "));
        assertThrows(IllegalArgumentException.class, () -> message.edit(""));
        assertThrows(IllegalArgumentException.class, () -> message.edit(null));
    }

}