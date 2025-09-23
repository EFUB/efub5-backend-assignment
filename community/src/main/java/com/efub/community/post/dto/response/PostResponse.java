package com.efub.community.post.dto.response;

import com.efub.community.post.domain.Post;
import java.time.LocalDateTime;

public record PostResponse(Long postId, String nickName, Boolean anonymous, String content,
                           LocalDateTime createdAt, LocalDateTime modifiedAt) {
    public static PostResponse from(Post post) {
        return new PostResponse(
                post.getId(),
                post.getNickname(),
                post.getAnonymous(),
                post.getContent(),
                post.getCreatedAt(),
                post.getModifiedAt()
        );
    }
}