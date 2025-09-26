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
        Member member = 기본_멤버_생성();
        Board board = 기본_게시판_생성(member, keyword);
        searchByNameAndAssert(keyword, List.of(board), keyword);
    }

    @Test
    void 검색어_일부_일치_시에도_반환한다() {
        List<Board> boards = List.of("컴공벗들모여라", "컴공스터디").stream()
                .map(name -> 기본_게시판_생성(기본_멤버_생성(), name))
                .toList();

        searchByNameAndAssert("컴공", boards, "컴공벗들모여라", "컴공스터디");
    }

    private Member 기본_멤버_생성(){
        return Member.builder()
                .email("test@email.com")
                .nickname("nickname")
                .studentId("studentId")
                .university("University")
                .build();
    }

    private Board 기본_게시판_생성(Member owner, String name){
        return Board.builder()
                .owner(owner)
                .name(name)
                .notice("공지")
                .description("설명")
                .build();
    }

    private void searchByNameAndAssert(String keyword, List<Board> stubBoards, String... expectedNames) {
        given(boardRepository.findByNameContaining(keyword)).willReturn(stubBoards);

        List<BoardSimpleResponse> result = boardSearchKeywordService.searchByName(keyword);

        assertEquals(expectedNames.length, result.size());
        for (String name : expectedNames) {
            assertTrue(result.stream().anyMatch(b -> b.getName().equals(name)));
        }
    }
}
