package com.sparta.board.dto;

import com.sparta.board.entity.Board;
import lombok.Getter;

import java.util.List;

@Getter
public class BoardCommentResponseDto {

    private BoardDto board;

    public BoardCommentResponseDto(Board board, List<CommentDto> commentList) {
        this.board = new BoardDto(board, commentList);
    }
}