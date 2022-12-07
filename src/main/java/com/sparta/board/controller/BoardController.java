package com.sparta.board.controller;

import com.sparta.board.dto.BoardRequestDto;
import com.sparta.board.dto.BoardResponseDto;
import com.sparta.board.dto.ResponseMsgDto;
import com.sparta.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;

    // 게시글 생성
    @PostMapping("api/boards")
    public BoardResponseDto createBoard(@RequestBody BoardRequestDto requestDto, HttpServletRequest request) {
        return boardService.create(requestDto, request);
    }

    // 게시글 조회
    @GetMapping("/api/boards")
    public List<BoardResponseDto> getBoard() {
        return boardService.getBoard();
    }

    // 게시글 선택 조회
    @GetMapping("api/boards/{id}")
    public BoardResponseDto getBoard(@PathVariable Long id) {
        return boardService.getBoard(id);
    }

    // 게시글 수정
    @PutMapping("api/boards/{id}")
    public BoardResponseDto updateBoard(@PathVariable long id, @RequestBody BoardRequestDto requestDto, HttpServletRequest request) {
        return boardService.updateBoard(id, requestDto, request);
    }

    // 게시글 삭제
    @DeleteMapping("api/boards/{id}")
    public ResponseEntity<ResponseMsgDto> deleteBoard(@PathVariable long id, HttpServletRequest request) {
        boardService.deleteBoard(id, request);
        return ResponseEntity.ok(new ResponseMsgDto(HttpStatus.OK,"삭제 성공"));
    }
}
