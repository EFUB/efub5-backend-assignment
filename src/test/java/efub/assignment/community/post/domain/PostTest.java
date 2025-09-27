package efub.assignment.community.post.domain;

import efub.assignment.community.board.domain.Board;
import efub.assignment.community.member.domain.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PostTest {

    private Member author;
    private Board board;
    private Post post;

    @BeforeEach
    void setUp() {
        author = Member.builder()
                .email("s2eojeong@gmail.com")
                .nickname("danchoo")
                .password("password1234!")
                .studentId("2276305")
                .university("Ewha")
                .build();
        board = Board.builder()
                .owner(author)
                .name("게시판 이름")
                .description("게시판 설명")
                .notice("게시판 공지")
                .build();
        post = Post.builder()
                .postId(1L)
                .board(board)
                .author(author)
                .anonymous(true)
                .content("게시글 내용")
                .build();
    }

    @Test
    void 게시글_생성_정상작동() {
        assertNotNull(author);
        assertEquals(1L, post.getPostId());
        assertEquals(board, post.getBoard());
        assertEquals(author, post.getAuthor());
        assertTrue(post.isAnonymous());
        assertEquals("게시글 내용", post.getContent());
    }

    @Test
    void 게시글_내용_수정_정상작동() {
        post.updateContent("게시글 내용 수정");
        assertEquals("게시글 내용 수정", post.getContent());
    }
}