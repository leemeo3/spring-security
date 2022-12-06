package com.sparta.board.service;

import com.sparta.board.dto.CommentDto;
import com.sparta.board.entity.Board;
import com.sparta.board.entity.Comment;
import com.sparta.board.repository.BoardRepository;
import com.sparta.board.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;

    public CommentDto addComment(CommentDto commentDto, Long id) {

        Optional<Board> optionalBoard = boardRepository.findById(id);
        Board board = optionalBoard.orElseThrow(
                () -> new IllegalArgumentException("게시글이 존재하지 않습니다.")
        );

        Comment comment = Comment.builder()
                .commentId(commentDto.getCommentId())
                .board(board)
                .commentUsername(commentDto.getCommentUsername())
                .commentContents(commentDto.getCommentContents())
                .build();

        return new CommentDto(commentRepository.save(comment));
    }
}
