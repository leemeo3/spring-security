package com.sparta.board.controller;

import com.sparta.board.dto.CommentDto;
import com.sparta.board.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/comment/{id}")
    public CommentDto addComment(@RequestBody CommentDto commentDto, @PathVariable Long id, HttpServletRequest request) {
        // 응답 보내기
        return commentService.addComment(commentDto, id, request);
    }

    @PutMapping("/comment/{id}")
    public CommentDto updateComment(@RequestBody CommentDto commentDto, @PathVariable Long id, HttpServletRequest request) {
        // 응답 보내기
        return commentService.updateComment(commentDto, id, request);
    }

    @DeleteMapping("/comment/{id}")
    public CommentDto deleteComment(@PathVariable Long id, HttpServletRequest request) {
        // 응답 보내기
        return commentService.deleteComment(id, request);
    }
}
