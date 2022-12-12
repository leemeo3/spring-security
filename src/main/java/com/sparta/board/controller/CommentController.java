package com.sparta.board.controller;

import com.sparta.board.dto.CommentDto;
import com.sparta.board.dto.ResponseMsgDto;
import com.sparta.board.security.UserDetailsImpl;
import com.sparta.board.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    // 댓글 추가
    @PostMapping("/comment/{id}")
    public CommentDto addComment(@RequestBody CommentDto commentDto,
                                 @PathVariable Long id,
                                 @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.addComment(commentDto, id, userDetails.getUser());
    }

    // 댓글 수정
    @PutMapping("/comment/{id}")
    public CommentDto updateComment(@PathVariable Long id,
                                    @RequestBody CommentDto commentDto,
                                    @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.updateComment(id, commentDto, userDetails.getUser());
    }

    // 댓글 삭제
    @DeleteMapping("/comment/{id}")
    public ResponseEntity<ResponseMsgDto> deleteComment(@PathVariable Long id,
                                                        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        commentService.deleteComment(id, userDetails.getUser());
        return ResponseEntity.ok(new ResponseMsgDto(HttpStatus.OK,"삭제 성공"));
    }
}