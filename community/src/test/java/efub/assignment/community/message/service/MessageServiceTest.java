package efub.assignment.community.message.service;

import efub.assignment.community.member.domain.Member;
import efub.assignment.community.message.domain.Message;
import efub.assignment.community.message.dto.response.MessageResponse;
import efub.assignment.community.message.repository.MessageRepository;
import efub.assignment.community.messageRooom.domain.MessageRoom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

class MessageServiceTest {

    @Mock
    private MessageRepository messageRepository;
    @InjectMocks
    private MessageService messageService;

    private Message existing;
    private Member sender;
    private MessageRoom room;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        sender = Mockito.mock(Member.class);
        given(sender.getMemberId()).willReturn(10L);

        room = Mockito.mock(MessageRoom.class);

        existing = Message.builder()
                .sender(sender)
                .messageRoom(room)
                .messageContent("원본")
                .isSentByMe(true)
                .build();
    }

    @Test
    void 쪽지수정_정상_보낸사람이_수정하면_내용변경() {
        // given
        given(messageRepository.findById(1L)).willReturn(Optional.of(existing));

        // when
        MessageResponse resp = messageService.updateMessage(1L, 10L, "수정본");

        // then
        assertEquals("수정본", resp.messageContent());
    }

    @Test
    void 쪽지수정_메시지없으면_예외() {
        // given
        given(messageRepository.findById(999L)).willReturn(Optional.empty());

        // when then
        assertThrows(NoSuchElementException.class,
                () -> messageService.updateMessage(999L, 10L, "수정본"));
    }

    @Test
    void 쪽지수정_보낸사람이아니면_예외() {
        // given
        given(messageRepository.findById(1L)).willReturn(Optional.of(existing));

        // when then
        assertThrows(IllegalStateException.class,
                () -> messageService.updateMessage(1L, 77L, "수정본"));
    }

    @Test
    void 쪽지수정_내용이공백이면_예외() {
        // given
        given(messageRepository.findById(1L)).willReturn(Optional.of(existing));

        // when then
        assertThrows(IllegalArgumentException.class,
                () -> messageService.updateMessage(1L, 10L, "  "));
    }

}