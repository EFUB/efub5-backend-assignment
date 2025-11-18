package com.efub_assignment.community.community.post.repository;

import com.efub_assignment.community.community.member.domain.QMember;
import com.efub_assignment.community.community.post.domain.Post;
import com.efub_assignment.community.community.post.domain.QPost;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CustomPostRepositoryImpl implements CustomPostRepository{

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Post> search(String keyword, String writerNickname){
        QPost post = QPost.post;
        QMember member = QMember.member;

        BooleanBuilder builder = new BooleanBuilder();

        if(writerNickname != null && !writerNickname.isBlank()){
            builder.and(post.writer.nickname.eq(writerNickname));
        }

        if(keyword != null && !keyword.isBlank()){
            builder.and(post.title.containsIgnoreCase(keyword)
                    .or(post.content.containsIgnoreCase(keyword)));
        }

        return queryFactory.selectFrom(post)
                .join(post.writer, member).fetchJoin()
                .where(builder)
                .orderBy(post.createdAt.desc())
                .fetch();
    }


}
