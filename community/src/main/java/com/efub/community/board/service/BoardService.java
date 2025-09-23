package com.efub.community.board.service;

import com.efub.community.board.domain.Board;
import com.efub.community.board.dto.request.BoardCreateRequest;
import com.efub.community.board.repository.BoardRepository;
import com.efub.community.global.exception.BlogException;
import com.efub.community.global.exception.ExceptionCode;
import com.efub.community.member.domain.entity.Member;
import com.efub.community.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public Long createBoard(BoardCreateRequest boardCreateRequest) {
        Long memberId = boardCreateRequest.memberId();
        Member owner = findByMemberId(memberId);
        Board newBoard = boardCreateRequest.toEntity(owner);
        boardRepository.save(newBoard);
        return newBoard.getId();
    }

    private Member findByMemberId(Long memberId) {
        return memberRepository.findByMemberId(memberId)
                .orElseThrow(()-> new BlogException(ExceptionCode.MEMBER_NOT_FOUND));
    }
}
