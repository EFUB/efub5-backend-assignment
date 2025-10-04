package efub.assignment.community.post;

import efub.assignment.community.post.dto.request.PostCreateRequestDto;

public class PostFixture {

    public static PostCreateRequestDto POST_CREATE_REQUEST(Long boardId, Long authorId){
        return new PostCreateRequestDto(boardId,
        false,
        authorId,
        "게시물 내용");
    }

    public static PostCreateRequestDto POST_CREATE_REQUEST_BLANK_CONTENT(Long boardId, Long authorId){
        return new PostCreateRequestDto(boardId,
                false,
                authorId,
                "");
    }
}
