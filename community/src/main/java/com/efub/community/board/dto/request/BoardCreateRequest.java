package com.efub.community.board.dto.request;

import com.efub.community.board.domain.Board;
import com.efub.community.member.domain.entity.Member;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

// record : 불변성 보장(모든 필드 final) + getter 등 자동제공
public record BoardCreateRequest(@NotNull Long memberId, // 계정 아이디
                                @NotBlank(message = "제목을 입력해야 합니다.") String boardTitle,
                                @NotBlank(message = "설명을 입력해야 합니다.") String description,
                                String notice,
                                @NotNull Member ownerNickname) {
    public Board toEntity(Member member) {
        return Board.builder()
                .title(boardTitle)
                .description(description)
                .notice(notice)
                .owner(member)
                .build();
    }
}
