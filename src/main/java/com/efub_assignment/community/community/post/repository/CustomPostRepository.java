package com.efub_assignment.community.community.post.repository;

import com.efub_assignment.community.community.post.domain.Post;

import java.util.List;

public interface CustomPostRepository {

    List<Post> search(String keyword, String writerNickname);
}
