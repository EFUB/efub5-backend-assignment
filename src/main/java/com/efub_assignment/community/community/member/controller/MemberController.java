package com.efub_assignment.community.community.member.controller;

import com.efub_assignment.community.community.member.dto.request.MemberRequestDto;
import com.efub_assignment.community.community.member.dto.response.MemberResponseDto;
import com.efub_assignment.community.community.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController //rest api의 컨트롤러로 사용할거임
@RequestMapping("/members") //메서드 경로를 지정. 이 클래스의 모든 메서드 경로 앞에 /members 가 붙음
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    // 멤버 생성 Post
    @PostMapping
    public ResponseEntity<MemberResponseDto> createMember(@RequestBody @Valid MemberRequestDto requestDto){
        // 1. 서비스를 호출하여 멤버를 생성하고 응답 DTO를 받는다.
        MemberResponseDto responseDto = memberService.createMember(requestDto);
        // 2. 201 Created 상태 코드와 함께 응답 DTO를 body에 담아 반환한다.
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

//    // 닉네임 수정 patch
//    @PatchMapping("/profile/{memberId}")
//    public ResponseEntity<MemberResponseDto> updateMember(@PathVariable("memberId") Long memberId, @RequestBody @Valid MemberRequestDto requestDto){
//        MemberResponseDto responseDto = memberService.updateMember(memberId, requestDto);
//        return ResponseEntity.ok(responseDto);
//    }
//
//    // 멤버 조회 get
//    @GetMapping("/{memberId}")
//    public ResponseEntity<MemberResponseDto> getMember(@PathVariable("memberId") Long memberId){
//        MemberResponseDto responseDto = memberService.getMember(memberId);
//        return ResponseEntity.ok(responseDto);
//    }
//
//    // 멤버 논리적 삭제 patch
//    @PatchMapping("/{memberId}")
//    public ResponseEntity<String> deleteMember(@PathVariable("memberId") Long memberId){
//        memberService.deleteMember(memberId);
//        return ResponseEntity.ok("삭제가 완료되었습니다.");
//    }

}


