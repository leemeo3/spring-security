package com.sparta.board.controller;

import com.sparta.board.dto.*;
import com.sparta.board.security.UserDetailsImpl;
import com.sparta.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;

    // 게시글 생성
    @PostMapping("api/boards")
    public BoardResponseDto createBoard(@RequestBody BoardRequestDto requestDto,
                                        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return boardService.create(requestDto, userDetails.getUser());
    }

    // 게시글 조회
    @GetMapping("/api/boards")
    public ResponseEntity<BoardListResponseDto> getBoardList() {
        return ResponseEntity.ok().body(boardService.getBoardList());
    }

    // 게시글 선택 조회
    @GetMapping("api/boards/{id}")
    public ResponseEntity<BoardCommentResponseDto> getBoardOne(@PathVariable Long id) {
        return ResponseEntity.ok().body(boardService.getBoard(id));
    }

    // 게시글 수정
    @PutMapping("api/boards/{id}")
    public BoardResponseDto updateBoard(@PathVariable long id,
                                        @RequestBody BoardRequestDto requestDto,
                                        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return boardService.updateBoard(id, requestDto, userDetails.getUser());
    }

    // 게시글 삭제
    @DeleteMapping("api/boards/{id}")
    public ResponseEntity<ResponseMsgDto> deleteBoard(@PathVariable long id,
                                                      @AuthenticationPrincipal UserDetailsImpl userDetails) {
        boardService.deleteBoard(id, userDetails.getUser());
        return ResponseEntity.ok(new ResponseMsgDto(HttpStatus.OK,"삭제 성공"));
    }
}
