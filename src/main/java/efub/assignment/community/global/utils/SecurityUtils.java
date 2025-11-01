package efub.assignment.community.global.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {

    public static String getCurrentUserEmail() {
        // SecurityContext에서 현재 인증 정보를 가져옴.
        Authentication authenication = SecurityContextHolder.getContext().getAuthentication();

        // 인증정보가 없어가 사용자 이름(이메일)이 없는 경우, null 반환
        if (authenication == null || authenication.getName() == null) {
            return null;
        }
        return authenication.getName();
    }
}