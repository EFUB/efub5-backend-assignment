package efub.assignment.community.Friend;


import efub.assignment.community.friend.domain.Friend;
import efub.assignment.community.friend.domain.FriendStatus;
import efub.assignment.community.friend.dto.FriendCreateRequestDto;
import efub.assignment.community.friend.repository.FriendRepository;
import efub.assignment.community.friend.service.FriendService;

import efub.assignment.community.member.entity.Member;
import efub.assignment.community.member.repository.MembersRepository;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Field;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

public class FriendServiceTest {

    @Mock
    private FriendRepository friendRepository;
    @Mock
    private MembersRepository memberRepository;

    @InjectMocks
    private FriendService friendService;

    private Validator validator;
    private Friend testFriend;
    Member memberLow = member(1L, "01", "nick1", "a@test.com");
    Member memberHigh = member(2L, "02", "nick2", "b@test.com");


    @BeforeEach
    void setFriend(){
        MockitoAnnotations.openMocks(this);

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        testFriend = Friend.builder()
                .memberLow(memberLow)
                .memberHigh(memberHigh)
                .status(FriendStatus.Accepted)
                .requestedBy(memberLow)
                .build();

    }

    // 중복 요청 예외
    @Test
    void 이미_친구_관계가_존재하면_예외발생(){
        //given
        FriendCreateRequestDto requestDto = new FriendCreateRequestDto(1L, 2L);
        given(memberRepository.findById(1L)).willReturn(Optional.of(memberLow));
        given(memberRepository.findById(2L)).willReturn(Optional.of(memberHigh));
        given(friendRepository.findByMemberLowAndMemberHigh(memberLow, memberHigh))
                .willReturn(true);

        //when & then
        assertThrows(IllegalArgumentException.class, ()->friendService.createFriend(requestDto));
    }

    // Reflection > MemberId 생성
    private Member member(Long id, String studentId, String nickname, String email) {
        Member member = new Member(studentId, "univ", nickname, email, "pw");
        try {
            Field f = Member.class.getDeclaredField("memberId");
            f.setAccessible(true);
            f.set(member, id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return member;
    }
}