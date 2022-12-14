package com.sparta.board.controller;

import com.sparta.board.dto.ResponseMsgDto;
import com.sparta.board.security.UserDetailsImpl;
import com.sparta.board.service.BoardService;
import com.sparta.board.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/")
public class LikeController {
    private final CommentService commentService;
    private final BoardService boardService;

    // 게시글 좋아요
    @PostMapping("boards/like/{id}")
    public ResponseEntity<ResponseMsgDto> LikeBoard(@PathVariable long id,
                                                    @AuthenticationPrincipal UserDetailsImpl userDetails) {
        boardService.LikeBoard(id, userDetails.getUser());
        return ResponseEntity.ok(new ResponseMsgDto(HttpStatus.OK,"게시글 좋아요 성공"));
    }

    // 게시글 좋아요 취소
    @DeleteMapping("boards/like/{id}")
    public ResponseEntity<ResponseMsgDto> deleteLikeBoard(@PathVariable long id,
                                                          @AuthenticationPrincipal UserDetailsImpl userDetails) {
        boardService.deleteLikeBoard(id, userDetails.getUser());
        return ResponseEntity.ok(new ResponseMsgDto(HttpStatus.OK,"게시글 좋아요 삭제"));
    }

    // 댓글 좋아요
    @PostMapping("comment/like/{id}")
    public ResponseEntity<ResponseMsgDto> LikeComment(@PathVariable long id,
                                                    @AuthenticationPrincipal UserDetailsImpl userDetails) {
        commentService.LikeComment(id, userDetails.getUser());
        return ResponseEntity.ok(new ResponseMsgDto(HttpStatus.OK,"댓글 좋아요 성공"));
    }

    // 댓글 좋아요 취소
    @DeleteMapping("comment/like/{id}")
    public ResponseEntity<ResponseMsgDto> deleteLikeComment(@PathVariable long id,
                                                          @AuthenticationPrincipal UserDetailsImpl userDetails) {
        commentService.deleteLikeComment(id, userDetails.getUser());
        return ResponseEntity.ok(new ResponseMsgDto(HttpStatus.OK,"댓글 좋아요 삭제"));
    }
}
