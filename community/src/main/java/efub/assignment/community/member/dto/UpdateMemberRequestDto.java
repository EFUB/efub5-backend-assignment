package efub.assignment.community.member.dto;

//멤버 수정 요청 DTO

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateMemberRequestDto {

    @NotBlank
    private String email;
    private String password;
    private String nickname;
    private String university;

    @NotNull
    private Long studentId;
}
