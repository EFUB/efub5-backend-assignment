package efub.assignment.community.message.service;

import efub.assignment.community.member.domain.Member;
import efub.assignment.community.member.service.MemberService;
import efub.assignment.community.message.domain.Message;
import efub.assignment.community.message.dto.request.MessageCreateRequest;
import efub.assignment.community.message.dto.response.MessageInfoResponse;
import efub.assignment.community.message.dto.response.MessageListResponse;
import efub.assignment.community.message.dto.response.MessageResponse;
import efub.assignment.community.message.repository.MessageRepository;
import efub.assignment.community.messageRooom.domain.MessageRoom;
import efub.assignment.community.messageRooom.service.MessageRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final MemberService memberService;
    private final MessageRoomService messageRoomService;

    // 쪽지 생성
    public MessageResponse createMessage(MessageCreateRequest request){
        Member sender = memberService.findByMemberId(request.senderId());
        MessageRoom messageRoom = messageRoomService.findByMessageRoomId(request.messageRoomId());

        Message message = request.toEntity(sender, messageRoom);
        messageRepository.save(message);

        return MessageResponse.from(message);
    }

    // 쪽지 조회
    public MessageListResponse getMessages(Long messageRoomId, Long viewerId){
        MessageRoom messageRoom = messageRoomService.findByMessageRoomId(messageRoomId);
        Member viewer = memberService.findByMemberId(viewerId);

        Member opponent = messageRoom.getSender().equals(viewer)? messageRoom.getReceiver():messageRoom.getSender();

        List<Message> messageList = messageRepository.findAllByMessageRoomOrderByCreatedAt(messageRoom);
        List<MessageInfoResponse> messages = messageList.stream()
                .map(msg -> new MessageInfoResponse(
                        msg.getMessageContent(),
                        msg.getCreatedAt(),
                        msg.getSender().equals(viewer)  // 보낸 사람 == 나?
                ))
                .toList();

        return new MessageListResponse(messageRoomId, opponent.getMemberId(), messages);
    }

    // 쪽지 수정
    @Transactional
    public MessageResponse updateMessage(Long messageId, Long editorId, String newContent) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new NoSuchElementException("메시지를 찾을 수 없습니다: " + messageId));

        // 보낸 사람만 수정 가능
        Long senderId = message.getSender().getMemberId();
        if (!senderId.equals(editorId)) {
            throw new IllegalStateException("작성자만 메시지를 수정할 수 있습니다.");
        }

        message.edit(newContent);
        messageRepository.save(message);
        return MessageResponse.from(message);
    }



}


