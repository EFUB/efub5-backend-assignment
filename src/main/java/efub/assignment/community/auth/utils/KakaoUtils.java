package efub.assignment.community.auth.utils;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class KakaoUtils {
    private final String clientId;
    private final String redirectUri;
    private final String tokenUrl;
    private final String userInfoUrl;

    public KakaoUtils(@Value("${kakao.client-id}") String clientId,
                      @Value("${kakao.redirect-uri}") String redirectUri,
                      @Value("${kakao.provider.kakao.token-uri}") String tokenUrl,
                      @Value("${kakao.provider.kakao.user-info-uri}") String userInfoUrl) {
        this.clientId = clientId;
        this.redirectUri = redirectUri;
        this.tokenUrl = tokenUrl;
        this.userInfoUrl = userInfoUrl;
    }
}