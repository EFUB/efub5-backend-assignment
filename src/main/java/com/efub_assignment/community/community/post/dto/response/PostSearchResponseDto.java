package com.efub_assignment.community.community.post.dto.response;


import com.efub_assignment.community.community.post.domain.Post;

public record PostSearchResponseDto(
        Long postId,
        String title,
        String content,
        String writerNickname,
        Long viewCount
){
    public PostSearchResponseDto(Post post){
        this(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getWriter().getNickname(),
                post.getViewCount()
        );
    }
}
