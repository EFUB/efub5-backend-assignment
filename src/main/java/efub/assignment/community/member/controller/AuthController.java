package efub.assignment.community.member.controller;

import efub.assignment.community.global.utils.SecurityUtils;
import efub.assignment.community.member.domain.Member;
import efub.assignment.community.member.dto.request.TokenRequestDto;
import efub.assignment.community.member.dto.response.TokenResponseDto;
import efub.assignment.community.member.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    //현재 인증된 사용자 이메일 조회
    @GetMapping("/me")
    public ResponseEntity<String> getEmail(){
        return ResponseEntity.ok(SecurityUtils.getCurrentUserEmail());
    }

    @PostMapping("/reissue")
    public ResponseEntity<TokenResponseDto> reissue(@RequestBody TokenRequestDto requestDto) {
        return ResponseEntity.ok(
                authService.reissueAccessToken(requestDto.getRefreshToken())
        );
    }

}
