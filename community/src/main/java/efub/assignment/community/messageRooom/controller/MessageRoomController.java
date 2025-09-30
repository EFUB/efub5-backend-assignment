package efub.assignment.community.messageRooom.controller;

import efub.assignment.community.board.dto.request.BoardCreateRequest;
import efub.assignment.community.messageRooom.dto.request.MessageRoomCheckRequest;
import efub.assignment.community.messageRooom.dto.request.MessageRoomCreateRequest;
import efub.assignment.community.messageRooom.dto.response.MessageRoomIdResponse;
import efub.assignment.community.messageRooom.dto.response.MessageRoomListResponse;
import efub.assignment.community.messageRooom.dto.response.MessageRoomResponse;
import efub.assignment.community.messageRooom.service.MessageRoomService;
import efub.assignment.community.post.dto.request.PostCreateRequest;
import efub.assignment.community.post.dto.response.PostResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/message-rooms")
public class MessageRoomController {

    private final MessageRoomService messageRoomService;

    // 쪽지방 생성
    @PostMapping
    public ResponseEntity<MessageRoomResponse> createMessageRoom(@Valid @RequestBody MessageRoomCreateRequest request) {
        MessageRoomResponse response = messageRoomService.createMessageRoom(request);
        return ResponseEntity
                .created(URI.create("/message-rooms/" + response.messageRoomId()))
                .body(response);
    }

    // 보내려는 사람과의 채팅방이 존재하는지 여부 조회
    @PostMapping("/check")
    public ResponseEntity<MessageRoomIdResponse> checkMessageRoom(@RequestBody MessageRoomCheckRequest request) {
        Long id = messageRoomService.findExistingMessageRoomId(request);
        return ResponseEntity.ok(new MessageRoomIdResponse(id));
    }

    // 특정 멤버가 참여하고 있는 채팅방 목록 조회
    @GetMapping
    public ResponseEntity<MessageRoomListResponse> getRooms(@RequestParam("memberId") Long memberId) {
        return ResponseEntity.ok(messageRoomService.getMessageRoomsByMember(memberId));
    }



    // 쪽지방 삭제
    @DeleteMapping("/{messageRoomId}")
    public ResponseEntity<String> deleteMessageRoom(@PathVariable("messageRoomId") Long messageRoomId) {
        messageRoomService.deleteMessageRoom(messageRoomId);
        return ResponseEntity.ok("쪽지방 삭제 성공");
    }




}
