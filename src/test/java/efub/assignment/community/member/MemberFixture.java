package efub.assignment.community.member;

import efub.assignment.community.member.dto.request.MemberRequestDto;

public class MemberFixture {

    public static MemberRequestDto MEMBER_CREATE_REQUEST(){
        return new MemberRequestDto(
            "123456", "EwhaUniversity", "테스트닉", "test@test.com", "!123Abc123123456789"
        );
    }

    public static MemberRequestDto MEMBER_CREATE_REQUEST_NICKNAME_TOO_LONG(){
        return new MemberRequestDto(
                "123456", "EwhaUniversity", "이건너무나도긴닉네임", "test@test.com", "!123Abc123123456789"
        );
    }
}
