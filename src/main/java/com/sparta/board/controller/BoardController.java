package com.sparta.board.controller;

import com.sparta.board.dto.BoardRequestDto;
import com.sparta.board.dto.BoardResponseDto;
import com.sparta.board.dto.StatusResponseDto;
import com.sparta.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;

    @PostMapping("api/boards")
    public StatusResponseDto createBoard(@RequestBody BoardRequestDto requestDto, HttpServletRequest request) {
        try {
            return boardService.create(requestDto, request);
        }catch (Exception e) {
            return new StatusResponseDto(false,400, e.getMessage());
        }
    }

    @GetMapping("/api/boards")
    public List<BoardResponseDto> getBoard() {
        return boardService.getBoard();
    }

    @GetMapping("api/boards/{id}")
    public BoardResponseDto getBoard(@PathVariable Long id) {
        return boardService.getBoard(id);
    }

    @PutMapping("api/boards/{id}")
    public StatusResponseDto updateBoard(@PathVariable long id, @RequestBody BoardRequestDto requestDto, HttpServletRequest request) {
        try {
            return boardService.updateBoard(id, requestDto, request);
        }catch (Exception e) {
            return new StatusResponseDto(false,400, e.getMessage());
        }
    }

    @DeleteMapping("api/boards/{id}")
    public BoardResponseDto deleteBoard(@PathVariable long id, HttpServletRequest request) {
        return boardService.deleteBoard(id, request);
    }
}
