package efub.assignment.community.friend.controller;


import efub.assignment.community.friend.dto.FriendCreateRequestDto;
import efub.assignment.community.friend.dto.FriendCreateResponseDto;
import efub.assignment.community.friend.repository.FriendRepository;
import efub.assignment.community.friend.service.FriendService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/friends")
@RequiredArgsConstructor
public class FriendController {
    private FriendService friendService;
    private FriendRepository friendRepository;

    // 친구 생성
    public ResponseEntity<FriendCreateResponseDto> createFriend(@RequestBody @Valid FriendCreateRequestDto requestDto){
        FriendCreateResponseDto responseDto = friendService.createFriend(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

}