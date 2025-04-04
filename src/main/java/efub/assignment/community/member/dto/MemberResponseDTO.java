package efub.assignment.community.member.dto;


import efub.assignment.community.member.domain.Member;

// 회원 조회 시 응답 DTO
public class MemberResponseDTO {
    private Long memberId;
    private int studentNumber;
    private String nickname;
    private String school;
    private String email;

    public MemberResponseDTO(Long memberId, int studentNumber, String nickname, String school, String email){
        this.memberId = memberId;
        this.studentNumber = studentNumber;
        this.nickname = nickname;
        this.school = school;
        this.email = email;
    }

    public Long getMemberId(){
        return memberId;
    }

    public int getStudentNumber(){
        return studentNumber;
    }

    public String getNickname(){
        return nickname;
    }

    public String getSchool(){
        return school;
    }

    public String getEmail(){
        return email;
    }
}
