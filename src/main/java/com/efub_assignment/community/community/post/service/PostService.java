package com.efub_assignment.community.community.post.service;

import com.efub_assignment.community.community.board.domain.Board;
import com.efub_assignment.community.community.board.repository.BoardRepository;
import com.efub_assignment.community.community.member.domain.Member;
import com.efub_assignment.community.community.member.repository.MemberRepository;
import com.efub_assignment.community.community.post.domain.Post;
import com.efub_assignment.community.community.post.domain.PostLike;
import com.efub_assignment.community.community.post.dto.request.PostCreateRequest;
import com.efub_assignment.community.community.post.dto.request.PostUpdateRequest;
import com.efub_assignment.community.community.post.dto.response.PostListResponse;
import com.efub_assignment.community.community.post.dto.response.PostResponse;
import com.efub_assignment.community.community.post.dto.response.PostSearchResponseDto;
import com.efub_assignment.community.community.post.dto.summary.PostSummary;
import com.efub_assignment.community.community.post.repository.PostLikeRepository;
import com.efub_assignment.community.community.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;
    private final PostLikeRepository postLikeRepository;

    @Transactional
    public Long createPost(PostCreateRequest postCreateRequest){
        Long memberId =postCreateRequest.memberId();
        Long boardId = postCreateRequest.boardId();

        Member writer = findByMemberId(memberId);
        Board board = findByBoardId(boardId);

        Post newPost = postCreateRequest.toEntity(writer, board);
        postRepository.save(newPost);
        boardRepository.increasePostCount(board.getId());

        return newPost.getId();
    }

    @Transactional
    public PostResponse getPost(Long postId){
        postRepository.increaseViewCount(postId);
        Post post = findByPostId(postId);
        return PostResponse.from(post);
    }

    @Transactional(readOnly = true)
    public PostListResponse getAllPosts(){
        List<PostSummary> postSummaries = postRepository.findByOrderByCreatedAtDesc().stream()
                .map(PostSummary::from).toList();
        return new PostListResponse(postSummaries, postRepository.count());
    }

    @Transactional
    public void updatePostContent(Long postId, PostUpdateRequest request, Long memberId, String password){
        Post post = findByPostId(postId);
        Member member = findByMemberId(memberId);
        authorizePostWriter(post, member, password);
        post.changeContent(request.content());
    }

    @Transactional
    public void deletePost(Long postId, Long memberId, String password){
        Post post = findByPostId(postId);
        Member member = findByMemberId(memberId);
        authorizePostWriter(post, member, password);
        postRepository.delete(post);
    }

    //검색 기능 추가
    @Transactional(readOnly = true)
    public List<PostSearchResponseDto> searchPost(String keyword, String writerNickname){
        return postRepository.search(keyword, writerNickname).stream()
                .map(PostSearchResponseDto::new)
                .toList();
    }

    //게시글 좋아요 등록
    @Transactional
    public void likePost(Long postId, Long memberId){
        Post post = findByPostId(postId);
        Member member = findByMemberId(memberId);
        //좋아요가 이미 존재하는지 여부 확인
        if(postLikeRepository.existsByPostAndMember(post,member)){
            throw new IllegalArgumentException("좋아요가 이미 있습니다.");
        }
        PostLike like = PostLike.builder()
                .post(post)
                .member(member)
                .build();
        postLikeRepository.save(like);
    }

    //게시글 좋아요 취소
    @Transactional
    public void unlikePost(Long postId, Long memberId){
        Post post = findByPostId(postId);
        Member member = findByMemberId(memberId);
        PostLike like = postLikeRepository.findByPostAndMember(post, member)
                .orElseThrow(()-> new IllegalArgumentException("좋아요가 발견되지 않습니다."));
        postLikeRepository.delete(like);
    }
    @Transactional
    public Post findByPostId(Long postId){
        return postRepository.findById(postId)
                .orElseThrow(()-> new NoSuchElementException("게시글을 찾을 수 없습니다."));
    }

    private Member findByMemberId(Long memberId){
        return memberRepository.findByMemberId(memberId)
                .orElseThrow(()-> new NoSuchElementException("회원을 찾을 수 없습니다."));
    }

    private Board findByBoardId(Long boardId){
        return boardRepository.findById(boardId)
                .orElseThrow(()-> new NoSuchElementException("게시판이 존재하지 않습니다."));
    }

    private void authorizePostWriter(Post post, Member member, String password){
        if(!post.getWriter().equals(member) || !post.getWriter().getPassword().equals(password)){
            throw new IllegalArgumentException("작성자가 아닙니다.");
        }
    }
}

