package com.efub.community.post.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import com.efub.community.post.domain.Post;
import com.efub.community.member.domain.entity.Member;

public record PostCreateRequest(@NotNull Long memberId,// 계정 아이디
        @NotBlank(message = "닉네임을 입력하세요.") String nickname, // 닉네임 (빈 내용 x)
        @NotNull Boolean anonymous, // 익명 여부 (null x)
        @NotBlank(message = "내용을 입력하세요.") String content // 내용 (빈 내용 x)
) {

    public com.efub.community.post.domain.Post toEntity(Member writer) {
        return Post.builder()
                .nickname(nickname)
                .content(content)
                .anonymous(anonymous)
                .writer(writer)
                .build();
    }
}