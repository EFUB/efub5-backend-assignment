package efub.assignment.community.post.service;

import efub.assignment.community.board.domain.Board;
import efub.assignment.community.member.domain.Member;
import efub.assignment.community.member.repository.MemberRepository;
import efub.assignment.community.post.domain.Post;
import efub.assignment.community.post.domain.PostHeart;
import efub.assignment.community.post.dto.request.PostLikeDto;
import efub.assignment.community.post.repository.PostHeartRepository;
import efub.assignment.community.post.repository.PostRepository;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

class PostServiceTest {
    @InjectMocks
    private PostService postService;

    @Mock
    private PostHeartRepository postHeartRepository;

    @Mock
    private PostRepository postRepository;

    @Mock
    private MemberRepository memberRepository;

    private Member member;
    private Post post;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);

        member = Member.builder()
                .studentId("2271000")
                .university("ewha")
                .email("jettie@ewha.ac.kr")
                .nickname("제티")
                .password("1234")
                .build();

        post = Post.builder()
                .postId(1L)
                .board(Board.builder().name("게시판").owner(member).build())
                .content("얏호")
                .author(member)
                .anonymous(false)
                .build();
    }

    // 게시물 좋아요
    @Test
    void 게시글_좋아요() {
        // given
        given(postRepository.findById(1L)).willReturn(Optional.of(post));
        given(memberRepository.findById(1L)).willReturn(Optional.of(member));
        given(postHeartRepository.existsByPostAndMember(post, member)).willReturn(false);

        PostLikeDto dto = new PostLikeDto(1L, 1L);

        // when
        boolean result = postService.toggleHeart(dto);

        // then
        assertTrue(result); // 등록 성공
    }

    // 게시글 취소
    @Test
    void 좋아요_취소() {
        // given
        given(postRepository.findById(1L)).willReturn(Optional.of(post));
        given(memberRepository.findById(1L)).willReturn(Optional.of(member));
        given(postHeartRepository.existsByPostAndMember(post, member)).willReturn(true);

        PostLikeDto dto = new PostLikeDto(1L, 1L);

        // when
        boolean result = postService.toggleHeart(dto);

        // then
        assertFalse(result); // 취소 성공
    }

    // 좋아요 개수 조회
    @Test
    void 게시글_좋아요_개수_조회() {
        // given
        given(postRepository.findById(1L)).willReturn(Optional.of(post));
        given(postHeartRepository.countByPost(post)).willReturn(5L);

        // when
        long count = postService.getHeartCount(1L);

        // then
        assertEquals(5L, count);
    }

    // 예외 처리: 게시글 없음
    @Test
    void 없는_게시글이면_예외발생() {
        // given
        given(postRepository.findById(99L)).willReturn(Optional.empty());

        PostLikeDto dto = new PostLikeDto(99L, 1L);

        // when & then
        assertThrows(IllegalArgumentException.class,
                () -> postService.toggleHeart(dto));
    }
}