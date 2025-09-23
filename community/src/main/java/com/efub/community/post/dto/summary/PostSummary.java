package com.efub.community.post.dto.summary;

import com.efub.community.post.domain.Post;

import java.time.LocalDateTime;

public record PostSummary(Long postId, String nickName, Boolean anonymous, String content,
                          LocalDateTime createdAt, LocalDateTime modifiedAt) {
    public static PostSummary from(Post post) {
        return new PostSummary(
                post.getId(),
                post.getNickname(),
                post.getAnonymous(),
                post.getContent(),
                post.getCreatedAt(),
                post.getModifiedAt()
        );
    }
}