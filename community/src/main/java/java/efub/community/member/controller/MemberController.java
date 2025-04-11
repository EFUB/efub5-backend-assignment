package java.efub.community.member.controller;

import java.efub.community.member.dto.CreateMemberResponseDto; // 회원가입 응답 DTO
import java.efub.community.member.dto.CreateMemberRequestDto; // 회원가입 요청 DTO
import java.efub.community.member.dto.MemberResponseDto;
import java.efub.community.member.dto.MemberUpdateRequestDto; // 회원 정보 수정 요청 DTO
import java.efub.community.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService; // 서비스 클래스 이름 수정

    // 회원 조회: GET /members/{memberId}
    @GetMapping("/{memberId}")
    public ResponseEntity<MemberResponseDto> getMember(@PathVariable("memberId") Long memberId) {
        MemberResponseDto responseDto = memberService.getMember(memberId);
        return ResponseEntity.ok(responseDto);
    }

    // 회원가입: POST /members
    @PostMapping
    public ResponseEntity<CreateMemberResponseDto> createMember(@RequestBody @Valid CreateMemberRequestDto requestDto) {
        CreateMemberResponseDto responseDto = memberService.createMember(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    // 프로필(자기소개) 수정: PATCH /members/profile/{memberId}
    @PatchMapping("/profile/{memberId}")
    public ResponseEntity<MemberResponseDto> updateMember(@PathVariable("memberId") Long memberId, @RequestBody @Valid MemberUpdateRequestDto requestDto) {
        MemberResponseDto responseDto = memberService.updateMember(memberId, requestDto);
        return ResponseEntity.ok(responseDto);
    }

    // 회원 탈퇴: PATCH /members/{memberId} (논리적 삭제)
    @PatchMapping("/{memberId}")
    public ResponseEntity<String> deleteMember(@PathVariable("memberId") Long memberId) {
        memberService.deleteMember(memberId);
        return ResponseEntity.ok("message : 성공적으로 탈퇴되었습니다.");
    }
}