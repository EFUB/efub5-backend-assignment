package efub.assignment.community.post.domain;

import efub.assignment.community.board.domain.Board;
import efub.assignment.community.member.domain.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PostHeartTest {

    private Member member;
    private Board board;
    private Post post;
    private PostHeart postHeart;

    @BeforeEach
    void setUp() {
        member = Member.builder()
                .studentId("2271000")
                .university("ewha")
                .email("jettie@ewha.ac.kr")
                .nickname("제티")
                .password("1234")
                .build();

        board = Board.builder()
                .owner(member)
                .description("설명~")
                .notice("화이팅")
                .name("게시판")
                .build();

        post = Post.builder()
                .postId(1L)
                .board(board)
                .content("얏호")
                .author(member)
                .anonymous(false)
                .build();

        postHeart = PostHeart.builder()
                .post(post)
                .member(member)
                .build();
    }

    @Test
    void post_like_builder(){
        assertNotNull(postHeart);
        assertEquals(post, postHeart.getPost());
        assertEquals(member, postHeart.getMember());
    }

}