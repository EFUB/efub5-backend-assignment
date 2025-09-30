package efub.assignment.community.post.service;

import efub.assignment.community.board.domain.Board;
import efub.assignment.community.board.repository.BoardRepository;
import efub.assignment.community.member.domain.Member;
import efub.assignment.community.member.repository.MemberRepository;
import efub.assignment.community.post.domain.Post;
import efub.assignment.community.post.dto.request.PostCreateRequestDto;
import efub.assignment.community.post.dto.response.PostResponseDto;
import efub.assignment.community.post.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

public class PostServiceTest {
    @InjectMocks
    private PostService postService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private BoardRepository boardRepository;

    @Mock
    private PostRepository postRepository;

    private Member testAuthor;
    private Board testBoard;
    private Post testPost;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        testAuthor = Member.builder()
                .email("s2eojeong@gmail.com")
                .nickname("danchoo")
                .password("password1234!")
                .studentId("2276305")
                .university("Ewha")
                .build();
        testBoard = Board.builder()
                .owner(testAuthor)
                .name("게시판 이름")
                .description("게시판 설명")
                .notice("게시판 공지")
                .build();
        testPost = Post.builder()
                .postId(1L)
                .board(testBoard)
                .author(testAuthor)
                .anonymous(true)
                .content("게시글 내용")
                .build();
    }

    @Test
    void 게시글_생성_성공(){
        // given
        PostCreateRequestDto requestDto = new PostCreateRequestDto(
                testBoard.getBoardId(),
                false,
                testAuthor.getMemberId(),
                "게시글 내용"
        );
        given(memberRepository.findByMemberId(testAuthor.getMemberId())).willReturn(Optional.of(testAuthor));
        given(boardRepository.findByBoardId(testBoard.getBoardId())).willReturn(Optional.of(testBoard));
        given(postRepository.save(any(Post.class))).willReturn(testPost);

        // when
        PostResponseDto response = postService.createPost(requestDto);

        // then
        assertThat(response.getContent()).isEqualTo("게시글 내용");
        assertThat(response.getAuthorId()).isEqualTo(testAuthor.getMemberId());
        assertThat(response.getBoardId()).isEqualTo(testBoard.getBoardId());

        verify(boardRepository).findByBoardId(requestDto.boardId());
        verify(memberRepository).findByMemberId(requestDto.authorId());
        verify(postRepository).save(any(Post.class));
    }
}