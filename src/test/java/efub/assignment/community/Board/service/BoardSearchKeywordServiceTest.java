package efub.assignment.community.Board.service;

import efub.assignment.community.board.domain.Board;
import efub.assignment.community.board.dto.response.BoardSimpleResponse;
import efub.assignment.community.board.repository.BoardRepository;
import efub.assignment.community.board.repository.BoardSearchKeywordRepository;
import efub.assignment.community.board.service.BoardSearchKeywordService;
import efub.assignment.community.member.domain.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class BoardSearchKeywordServiceTest {
    @Mock
    private BoardRepository boardRepository;

    @Mock
    private BoardSearchKeywordRepository keywordRepository;

    @InjectMocks
    private BoardSearchKeywordService boardSearchKeywordService;

    private Member member;

    @BeforeEach
    void setupMember() {
        member = Member.builder()
                .email("test@email.com")
                .nickname("nickname")
                .studentId("studentId")
                .university("University")
                .build();
    }

    @Test
    void 게시판_이름_정확히_일치하면_검색된다(){
        String keyword = "컴공벗들모여라";
        Board board = 기본_게시판_생성(member, keyword);
        searchByNameAndAssert(keyword, List.of(board), keyword);
    }

    @Test
    void 검색어_일부_일치_시에도_반환한다() {
        List<Board> boards = Stream.of("컴공벗들모여라", "컴공스터디")
                .map(name -> 기본_게시판_생성(member, name))
                .toList();

        searchByNameAndAssert("컴공", boards, "컴공벗들모여라", "컴공스터디");
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
