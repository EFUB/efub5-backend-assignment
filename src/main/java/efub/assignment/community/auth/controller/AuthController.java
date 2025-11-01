package efub.assignment.community.auth.controller;

import efub.assignment.community.auth.dto.response.TokenResponseDto;
import efub.assignment.community.auth.service.AuthService;
import efub.assignment.community.auth.service.KakaoService;
import efub.assignment.community.global.utils.SecurityUtils;
import efub.assignment.community.member.domain.Member;
import efub.assignment.community.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;
    private final KakaoService kakaoService;

    //현재 인증된 사용자 이메일 조회
    @GetMapping("/me")
    public ResponseEntity<String> getEmail() {
        return ResponseEntity.status(HttpStatus.OK).body(SecurityUtils.getCurrentUserEmail());
    }

    @GetMapping("/kakao")
    public ResponseEntity<String> kakaoLogin(@RequestParam String code) {
        return ResponseEntity.ok(kakaoService.kakaoLogin(code));
    }
}