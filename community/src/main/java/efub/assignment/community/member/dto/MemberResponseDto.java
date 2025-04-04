package efub.assignment.community.member.dto;

//Member 조회 후 응답 DTO

import efub.assignment.community.member.domain.Member;
import lombok.Builder;
import lombok.Getter;

@Builder @Getter
public class MemberResponseDto {
    private Long memberId;
    private String email;
    private String nickname;
    private String university;
    private Long studentId;

    public static MemberResponseDto from(Member member){
        return MemberResponseDto.builder()
                .memberId(member.getMemberId())
                .email(member.getEmail())
                .nickname(member.getNickname())
                .university(member.getUniversity())
                .studentId(member.getStudentId())
                .build();
    }
}
