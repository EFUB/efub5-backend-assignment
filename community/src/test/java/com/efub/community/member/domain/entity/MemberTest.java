package com.efub.community.member.domain.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class MemberTest {

    @Test
    @DisplayName("회원생성 시 기본상태(ACTIVE) 초기화 검증")
    void member_default_status_test() {
        // given
        // Entity Builder를 통해 Member 객체 생성
        Member member = Member.builder()
                .email("test@lookup.com")
                .password("pw")
                .nickname("tester")
                .university("Uni")
                .studentId("1111")
                .build();

        // when
        MemberStatus currentStatus = member.getStatus();

        // then
        // 현재 status는 null이므로, ACTIVE를 기대하는 이 코드는 반드시 실패
        assertThat(currentStatus).isEqualTo(MemberStatus.ACTIVE);
    }

}