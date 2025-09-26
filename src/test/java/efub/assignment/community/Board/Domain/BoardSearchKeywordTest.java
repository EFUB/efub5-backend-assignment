package efub.assignment.community.Board.Domain;

import efub.assignment.community.board.domain.Board;
import efub.assignment.community.board.domain.BoardSearchKeyword;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class BoardSearchKeywordTest {

    @Test
    void 검색어가_null이면_예외발생(){
        assertThrows(IllegalArgumentException.class, () -> BoardSearchKeyword.builder().keyword(null).build());
    }

    @Test
    void 검색어가_빈문자열이면_예외발생(){
        assertThrows(IllegalArgumentException.class, () -> BoardSearchKeyword.builder().keyword("").build());
    }

    @Test
    void 일반적인_검색어면_엔티티생성(){
        BoardSearchKeyword keyword = BoardSearchKeyword.builder()
                .keyword("컴공벗")
                .build();
        assertNotNull(keyword);
        assertEquals("컴공벗", keyword.getKeyword());
    }
}
