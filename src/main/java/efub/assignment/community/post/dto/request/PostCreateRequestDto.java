package efub.assignment.community.post.dto.request;

import efub.assignment.community.board.domain.Board;
import efub.assignment.community.member.domain.Member;
import efub.assignment.community.post.domain.Post;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PostCreateRequestDto(@NotNull(message = "게시물 id를 입력해주세요.") Long boardId,
                                  @NotNull(message = "익명 여부를 선택해주세요.") boolean anonymous,
                                  @NotNull(message = "작성자 id를 입력해주세요.") Long authorId,
                                  @NotBlank(message = "내용을 입력해주세요.") String content) {
    public Post toEntity(Board board, Member author) {
        return Post.builder()
                .board(board)
                .anonymous(anonymous)
                .author(author)
                .content(content)
                .build();
    }
}
