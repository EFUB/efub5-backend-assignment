package efub.assignment.community.post.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import efub.assignment.community.board.domain.QBoard;
import efub.assignment.community.member.domain.QMember;
import efub.assignment.community.post.domain.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import efub.assignment.community.post.domain.QPost;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CustomPostRepositoryImpl implements  CustomPostRepository{

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Post> search(String keyword, String authorName, String boardName) {
        QPost post = QPost.post;
        QMember member = QMember.member;
        QBoard board = QBoard.board;

        BooleanBuilder builder = new BooleanBuilder();

        if(authorName != null && !authorName.isBlank()){
            builder.and(post.author.nickname.eq(authorName));
        }

        if(keyword != null && !keyword.isBlank()){
            builder.and(post.content.containsIgnoreCase(keyword));
        }

        if(boardName != null && !boardName.isBlank()){
            builder.and(post.board.name.eq(boardName));
        }

        return queryFactory.selectFrom(post)
                .join(post.author, member).fetchJoin()
                .join(post.board, board).fetchJoin()
                .where(builder)
                .orderBy(post.createdAt.desc())
                .fetch();
    }
}
