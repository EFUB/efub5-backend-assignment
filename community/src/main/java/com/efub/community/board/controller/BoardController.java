package com.efub.community.board.controller;

import com.efub.community.board.dto.request.BoardCreateRequest;
import com.efub.community.board.service.BoardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/boards")
public class BoardController {
    private final BoardService boardService;

    // 게시판 생성
    @PostMapping
    public ResponseEntity<Void> createBoard(@Valid @RequestBody BoardCreateRequest request) {
        Long id = boardService.createBoard(request);
        return ResponseEntity.created(URI.create("/boards/"+id)).build();
    }

    // 게시판지기 수정

    // 게시판 조회
    
    // 게시판 삭제

}
