package efub.assignment.community.board.repository;

import efub.assignment.community.board.domain.BoardSearchKeyword;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardSearchKeywordRepository extends JpaRepository<BoardSearchKeyword,Long> {
}
