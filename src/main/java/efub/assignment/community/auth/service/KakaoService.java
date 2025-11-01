package efub.assignment.community.auth.service;

import efub.assignment.community.auth.dto.response.KakaoTokenResponseDto;
import efub.assignment.community.auth.dto.response.TokenResponseDto;
import efub.assignment.community.auth.jwt.TokenProvider;
import efub.assignment.community.auth.utils.KakaoUtils;
import efub.assignment.community.member.domain.Member;
import efub.assignment.community.member.repository.MemberRepository;
import io.netty.handler.codec.http.HttpHeaderValues;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class KakaoService {

    private final KakaoUtils kakaoUtils;
    private final TokenProvider tokenProvider;
    private final MemberRepository memberRepository;

    public TokenResponseDto kakaoLogin(String code){
        // 1. 인가 코드로 AccessToken 받기
        String kakaoAccessToken = getAccessTokenFromKakao(code);

        // 2. 카카오 사용자 정보 요청
        Map<String, Object> kakaoUserInfo = getUserInfo(kakaoAccessToken);
        String nickname = (String) kakaoUserInfo.get("nickname");

        // 3. DB에 사용자 존재 여부 확인
        Member member = memberRepository.findByNickname(nickname)
                .orElseGet(() -> memberRepository.save(
                        Member.builder()
                                .email("")
                                .nickname(nickname)
                                .password("")
                                .studentId("")
                                .university("ewha")
                                .build()
                ));

        // 4. JWT 발급 및 redis에 저장
        String accessToken = tokenProvider.createAccessToken(member);
        String refreshToken = tokenProvider.createRefreshToken(member);
        tokenProvider.saveRefreshToken(member.getMemberId(), refreshToken);

        // 5. 응답 반환
        return TokenResponseDto.builder().accessToken(accessToken).build();
    }

    public String getAccessTokenFromKakao(String code) {

        KakaoTokenResponseDto kakaoTokenResponseDto = WebClient.create(kakaoUtils.getTokenUrl()).post()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .queryParam("grant_type", "authorization_code")
                        .queryParam("client_id", kakaoUtils.getClientId())
                        .queryParam("code", code)
                        .build(true))
                .header(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_X_WWW_FORM_URLENCODED.toString())
                .retrieve()
                //TODO : Custom Exception
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> Mono.error(new RuntimeException("Invalid Parameter")))
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse -> Mono.error(new RuntimeException("Internal Server Error")))
                .bodyToMono(KakaoTokenResponseDto.class)
                .block();


        log.info(" [Kakao Service] Access Token ------> {}", kakaoTokenResponseDto.getAccessToken());
        log.info(" [Kakao Service] Refresh Token ------> {}", kakaoTokenResponseDto.getRefreshToken());
        //제공 조건: OpenID Connect가 활성화 된 앱의 토큰 발급 요청인 경우 또는 scope에 openid를 포함한 추가 항목 동의 받기 요청을 거친 토큰 발급 요청인 경우
        log.info(" [Kakao Service] Id Token ------> {}", kakaoTokenResponseDto.getIdToken());
        log.info(" [Kakao Service] Scope ------> {}", kakaoTokenResponseDto.getScope());

        return kakaoTokenResponseDto.getAccessToken();
    }

    public Map<String, Object> getUserInfo(String accessToken) {
        Map<String, Object> userInfo = WebClient.create(kakaoUtils.getUserInfoUrl()).get()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        // 필요한 정보 추출 (email, nickname 등)
        Map<String, Object> kakaoAccount = (Map<String, Object>) userInfo.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");

        Map<String, Object> result = new HashMap<>();
        result.put("email", kakaoAccount.get("email"));
        result.put("nickname", profile.get("nickname"));
        return result;
    }
}
