package com.efub.community.post.service;

import com.efub.community.member.domain.entity.Member;
import com.efub.community.global.exception.BlogException;
import com.efub.community.global.exception.ExceptionCode;

import com.efub.community.member.repository.MemberRepository;
import com.efub.community.post.domain.Post;
import com.efub.community.post.dto.summary.PostSummary;
import com.efub.community.post.dto.request.PostCreateRequest;
import com.efub.community.post.dto.request.PostUpdateRequest;
import com.efub.community.post.dto.response.PostResponse;
import com.efub.community.post.dto.response.PostListResponse;
import com.efub.community.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public Long createPost(PostCreateRequest postCreateRequest) {
        Long memberId = postCreateRequest.memberId();
        Member writer = findByMemberId(memberId);
        Post newPost = postCreateRequest.toEntity(writer);
        postRepository.save(newPost);
        return newPost.getId();
    }

    @Transactional
    public PostResponse getPost(Long postId) {
        Post post = findByPostId((postId));
        return PostResponse.from(post);
    }

    @Transactional(readOnly = true) // 단순 조회만 하므로 리드온리 true
    public PostListResponse getAllPosts() {
        List<PostSummary> postSummaries = postRepository.findByOrderByCreatedAtDesc().stream()
                .map(PostSummary::from).toList();
        return new PostListResponse(postSummaries, postRepository.count());
    }

    @Transactional
    public void updatePostContent(Long postId, PostUpdateRequest request, Long memberId, String password) {
        // 게시물 불러오기
        Post post = findByPostId(postId);
        // 작성자 맞는 지 확인
        Member member = findByMemberId(memberId);
        authorizePostWriter(post, member, password);
        post.changeContent(request.content());
    }

    @Transactional
    public void deletePost(Long postId, Long memberId, String password) {
        Post post = findByPostId(postId);
        Member member = findByMemberId(memberId);
        authorizePostWriter(post, member, password);
        postRepository.delete(post);
    }

    private Post findByPostId(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(()-> new BlogException(ExceptionCode.POST_NOT_FOUND));
    }

    private Member findByMemberId(Long memberId) {
        return memberRepository.findByMemberId(memberId)
                .orElseThrow(()-> new BlogException(ExceptionCode.MEMBER_NOT_FOUND));
    }

    private void authorizePostWriter(Post post, Member member, String password) {
        if(!post.getWriter().equals(member) || !post.getWriter().getPassword().equals(password)){
            throw new BlogException(ExceptionCode.POST_MEMBER_MISMATCH);
        }
    }

}
