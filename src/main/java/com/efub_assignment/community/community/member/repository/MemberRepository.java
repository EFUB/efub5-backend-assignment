package com.efub_assignment.community.community.member.repository;

import com.efub_assignment.community.community.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    //이메일 중복 검사 쿼리
    boolean existsByEmail(String email);

    //학번 중복 검사 쿼리
    boolean existsByStudentId(String studentId);

    // 멤버 아이디로 조회
    Optional<Member> findByMemberId(Long memberId);
}
