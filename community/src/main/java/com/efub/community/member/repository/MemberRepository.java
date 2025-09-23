package com.efub.community.member.repository;

import com.efub.community.member.domain.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    // 이메일로 조회
    Optional<Member> findByEmail(String email);

    // 회원 ID로 조회
    Optional<Member> findByMemberId(Long memberId);

    // 이메일 존재 여부 확인
    boolean existsByEmail(String email);

}