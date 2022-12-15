package com.sparta.board.dto;

import com.sparta.board.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {
    // 댓글 응답 DTO
    private Long    commentId;          // ID
    private Long    mId;                // Board ID
    private String  commentUsername;    // 댓글유저아이디
    private String  commentContents;    // 댓글내용
    private int     likeCnt;            // 좋아요

    public CommentDto(Comment comment) {
        // entity -> Dto
        this.commentId       = comment.getCommentId();          // ID
        this.mId             = comment.getBoard().getId();      // Board ID
        this.commentUsername = comment.getCommentUsername();    // 댓글유저아이디
        this.commentContents = comment.getCommentContents();    // 댓글내용
        this.likeCnt         = comment.getLikesNum();
    }
}
