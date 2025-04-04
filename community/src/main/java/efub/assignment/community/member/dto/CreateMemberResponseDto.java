package efub.assignment.community.member.dto;

//Member 생성 후 응답 DTO

import efub.assignment.community.member.domain.Member;
import lombok.Builder;
import lombok.Getter;

@Builder @Getter
public class CreateMemberResponseDto {
    private Long memberId;
    private String email;
    private String nickname;
    private String university;
    private Long studentId;

    public static CreateMemberResponseDto from(Member member){
        return CreateMemberResponseDto.builder()
                .memberId(member.getMemberId())
                .email(member.getEmail())
                .nickname(member.getNickname())
                .university(member.getUniversity())
                .studentId(member.getStudentId())
                .build();
    }

}
