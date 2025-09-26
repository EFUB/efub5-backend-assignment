package efub.assignment.community.board.service;

import efub.assignment.community.board.domain.Board;
import efub.assignment.community.board.dto.response.BoardSimpleResponse;
import efub.assignment.community.board.repository.BoardRepository;
import efub.assignment.community.board.repository.BoardSearchKeywordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardSearchKeywordService {
    private final BoardSearchKeywordRepository boardSearchKeywordRepository;
    private final BoardRepository boardRepository;

    public List<BoardSimpleResponse> searchByName(String keyword) {
        return boardRepository.findByNameContaining(keyword).stream()
                .map(board -> new BoardSimpleResponse(board.getBoardId(), board.getName())).toList();
    }

}
