package com.efub_assignment.community.community.post.controller;

import com.efub_assignment.community.community.post.dto.request.PostCreateRequest;
import com.efub_assignment.community.community.post.dto.request.PostUpdateRequest;
import com.efub_assignment.community.community.post.dto.response.PostListResponse;
import com.efub_assignment.community.community.post.dto.response.PostResponse;
import com.efub_assignment.community.community.post.dto.response.PostSearchResponseDto;
import com.efub_assignment.community.community.post.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.connector.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    //게시물 생성
    @PostMapping
    public ResponseEntity<Void> createPost(@Valid @RequestBody PostCreateRequest request){
        Long postId = postService.createPost(request);
        return ResponseEntity.created(URI.create("/posts/"+postId)).build();
    }

    //게시물 목록 조회
    @GetMapping
    public ResponseEntity<PostListResponse> getAllPosts(){
        return ResponseEntity.ok(postService.getAllPosts());
    }

    //게시물 내용 조회
    @GetMapping("/{postId}")
    public ResponseEntity<PostResponse> getPost(@PathVariable("postId") Long postId){
        return ResponseEntity.ok(postService.getPost(postId));
    }

    //게시물 내용 수정
    @PatchMapping("/{postId}")
    public ResponseEntity<Void> updatePostContent(@PathVariable("postId") Long postId,
                                                  @RequestHeader("Auth-Id") Long memberId,
                                                  @RequestHeader("Auth-password") String password,
                                                  @Valid @RequestBody PostUpdateRequest postUpdateRequest){
        postService.updatePostContent(postId, postUpdateRequest, memberId, password);
        return ResponseEntity.noContent().build();
    }

    // 게시물 삭제
    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable("postId") Long postId,
                                           @RequestHeader("Auth-Id") Long memberId,
                                           @RequestHeader("Auth-password") String password){
        postService.deletePost(postId, memberId, password);
        return ResponseEntity.noContent().build();
    }

    //게시글 검색
    @GetMapping("/search")
    public ResponseEntity<List<PostSearchResponseDto>> searchPost(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "writer", required = false) String writerNickname
    ){
        return ResponseEntity.ok(postService.searchPost(keyword, writerNickname));
    }
    //게시글 좋아요
    @PostMapping("/{postId}/like")
    public ResponseEntity<String> likePost(@PathVariable("postId") Long postId,
                                           @RequestHeader("Auth-Id") Long memberId){
        postService.likePost(postId, memberId);
        return ResponseEntity.status(HttpStatus.CREATED).body("좋아요를 눌렀습니다.");
    }

    //게시글 좋아요 취소
    @DeleteMapping("/{postId}/like")
    public ResponseEntity<String> unlikePost(@PathVariable("postId") Long postId,
                                             @RequestHeader("Auth-Id") Long memberId) {
        postService.unlikePost(postId, memberId);
        return ResponseEntity.ok("좋아요가 취소되었습니다.");
    }
}
