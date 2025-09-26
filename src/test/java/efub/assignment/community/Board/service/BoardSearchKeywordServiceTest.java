package efub.assignment.community.Board.service;

import efub.assignment.community.board.domain.Board;
import efub.assignment.community.board.dto.response.BoardSimpleResponse;
import efub.assignment.community.board.repository.BoardRepository;
import efub.assignment.community.board.repository.BoardSearchKeywordRepository;
import efub.assignment.community.board.service.BoardSearchKeywordService;
import efub.assignment.community.member.domain.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;

@SpringBootTest
public class BoardSearchKeywordServiceTest {
    @MockBean
    private BoardRepository boardRepository;

    @MockBean
    private BoardSearchKeywordRepository keywordRepository;

    @Autowired
    private BoardSearchKeywordService boardSearchKeywordService;

    @Test
    void 게시판_이름_정확히_일치하면_검색된다(){
        String keyword = "컴공벗들모여라";
        Member member = Member.builder()
                .email("test@email.com")
                .nickname("nickname")
                .studentId("studentId")
                .university("University")
                .build();
        Board board = Board.builder()
                .owner(member)
                .name(keyword)
                .notice("모이셈")
                .description("description")
                .build();
        given(boardRepository.findByNameContaining(keyword))
                .willReturn(List.of(board));
        List<BoardSimpleResponse> result = boardSearchKeywordService.searchByName(keyword);

        assertEquals(1, result.size());
        assertEquals(keyword, result.get(0).getName());
    }

    @Test
    void 검색어_일부_일치_시에도_반환한다(){
        Member member = Member.builder()
                .email("test@email.com")
                .nickname("nickname")
                .studentId("studentId")
                .university("University")
                .build();

        Board board1 = Board.builder()
                .owner(member)
                .name("컴공벗들모여라")
                .notice("공지")
                .description("설명")
                .build();

        Board board2 = Board.builder()
                .owner(member)
                .name("컴공스터디")
                .notice("공지")
                .description("설명")
                .build();
        given(boardRepository.findByNameContaining("컴공"))
                .willReturn(List.of(board1, board2));
        List<BoardSimpleResponse> result = boardSearchKeywordService.searchByName("컴공");
        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(b -> b.getName().equals("컴공벗들모여라")));
        assertTrue(result.stream().anyMatch(b -> b.getName().equals("컴공스터디")));
    }
}
