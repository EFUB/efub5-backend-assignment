package efub.assignment.community.Board.Domain;

import efub.assignment.community.board.domain.BoardSearchKeyword;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class BoardSearchKeywordTest {

    @Test
    void 검색어가_null이면_예외발생(){
        assertThrows(IllegalArgumentException.class, () -> BoardSearchKeyword.builder().keyword(null).build());
    }
}
