package efub.assignment.community.member.domain;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.BAD_REQUEST, reason = "이미 존재하는 이메일 입니다.")
public class DuplicateMemberEmailException extends RuntimeException {
    public DuplicateMemberEmailException(String s) {
    }
}
