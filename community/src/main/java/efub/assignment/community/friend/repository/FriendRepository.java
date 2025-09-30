package efub.assignment.community.friend.repository;
import efub.assignment.community.friend.domain.Friend;
import efub.assignment.community.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FriendRepository extends JpaRepository<Friend, Long> {
    boolean findByMemberLowAndMemberHigh(Member a, Member b);

}