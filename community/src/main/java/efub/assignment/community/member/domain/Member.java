package efub.assignment.community.member.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

@Entity
@Getter
@Table(name="member")
public class Member {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long memberId;

    //멤버 이메일
    @Column(nullable = false, unique=true)
    private String email;

    //멤버 비밀번호
    @Column(nullable=false)
    private String password;

    //멤버 닉네임
    @Column(nullable=false, updatable=false)
    private String nickname;

    //멤버 학교
    @Column(nullable = false)
    private String university;

    //멤버 학번
    @Column(nullable = false)
    private Long studentId;

    //멤버 상태
    @Enumerated(EnumType.STRING)
    private MemberStatus status=MemberStatus.ACTIVE;

    //JPA용 기본 생성자 !!!
    protected Member() {}

    @Builder
    public Member(String email, String password, String nickname, String university, Long studentId){
        this.email=email;
        this.password=password;
        this.nickname=nickname;
        this.university=university;
        this.studentId=studentId;
    }

    public void updateMember(String email, String password, String nickname, String university, Long studentId){
        this.email=email;
        this.password=password;
        this.nickname=nickname;
        this.university=university;
        this.studentId=studentId;
    }

    public void changeStatus(MemberStatus status) {
        this.status = status;
    }

}
