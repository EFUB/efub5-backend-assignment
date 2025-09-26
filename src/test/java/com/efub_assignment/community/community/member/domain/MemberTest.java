package com.efub_assignment.community.community.member.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class MemberTest {

    @Test
    @DisplayName("멤버 닉네임 변경 성공")
    void updateNickname_Success(){
        // given
        Member member = Member.builder()
                .nickname("기존닉네임")
                .build();
        String newNickname = "새로운닉네임";

        // when
        // 로직이 비어있는 updateNickname 메서드 호출
        member.updateNickname(newNickname);

        // then
        assertThat(member.getNickname()).isEqualTo(newNickname);
    }

}