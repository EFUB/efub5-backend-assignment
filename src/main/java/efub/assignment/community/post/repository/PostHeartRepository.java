package efub.assignment.community.post.repository;

import efub.assignment.community.member.domain.Member;
import efub.assignment.community.post.domain.Post;
import efub.assignment.community.post.domain.PostHeart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostHeartRepository extends JpaRepository<PostHeart, Long> {
    boolean existsByPostAndMember(Post post, Member member);
    void deleteByPostAndMember(Post post, Member member);
    long countByPost(Post post);
}