package efub.assignment.community.member;

import efub.assignment.community.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
    // 이메일 존재 여부
    boolean existsByEmail(String email);

    // 학번 존재 여부
    boolean existsByStudentNumber(int studentNumber);
}
