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
import org.mockito.stubbing.Answer;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
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
    private List<Board> allBoards;

    @BeforeEach
    void setup() {
        member = Member.builder()
                .email("test@email.com")
                .nickname("nickname")
                .studentId("studentId")
                .university("University")
                .build();
        // 모든 테스트에서 사용할 게시판 리스트
        allBoards = Stream.of("컴공벗들모여라", "컴공스터디", "운동동아리")
                .map(name -> 기본_게시판_생성(member, name))
                .toList();

        // Stub: keyword 포함 게시판만 반환
        given(boardRepository.findByNameContaining(anyString()))
                .willAnswer((Answer<List<Board>>) invocation -> {
                    String keyword = invocation.getArgument(0);
                    return allBoards.stream()
                            .filter(b -> b.getName().contains(keyword))
                            .toList();
                });
    }

    @Test
    void 게시판_이름_정확히_일치하면_검색된다(){
        String keyword = "컴공벗들모여라";
        searchByNameAndAssert(keyword, keyword);
    }

    @Test
    void 검색어_일부_일치_시에도_반환한다() {
        searchByNameAndAssert("컴공", "컴공벗들모여라", "컴공스터디");
    }

    @Test
    void 검색어_일치하는_게시판_없으면_빈리스트(){
        String keyword = "없는키워드";
        searchByNameAndAssert(keyword);
    }

    private Board 기본_게시판_생성(Member owner, String name){
        return Board.builder()
                .owner(owner)
                .name(name)
                .notice("공지")
                .description("설명")
                .build();
    }

    private void searchByNameAndAssert(String keyword, String... expectedNames) {
        List<BoardSimpleResponse> result = boardSearchKeywordService.searchByName(keyword);

        assertEquals(expectedNames.length, result.size());
        for (String name : expectedNames) {
            assertTrue(result.stream().anyMatch(b -> b.getName().equals(name)));
        }
    }
}
