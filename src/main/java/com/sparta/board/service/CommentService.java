package com.sparta.board.service;

import com.sparta.board.dto.CommentDto;
import com.sparta.board.entity.*;
import com.sparta.board.jwt.JwtUtil;
import com.sparta.board.repository.BoardRepository;
import com.sparta.board.repository.CommentLikeRepository;
import com.sparta.board.repository.CommentRepository;
import com.sparta.board.repository.UserRepository;
import com.sparta.board.util.exception.ErrorCode;
import com.sparta.board.util.exception.RequestException;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;
    private final CommentLikeRepository commentLikeRepository;

    public CommentDto addComment(CommentDto commentDto, Long id, User user) {
        Optional<Board> optionalBoard = boardRepository.findById(id);
        Board board = optionalBoard.orElseThrow(
                () -> new RequestException(ErrorCode.NULL_CONTENTS_400)
        );
        Comment comment = Comment.builder()
                .commentId(commentDto.getCommentId())
                .board(board)
                .commentUsername(user.getUsername())
                .commentContents(commentDto.getCommentContents())
                .build();

        return new CommentDto(commentRepository.save(comment));
    }

    @Transactional
    public CommentDto updateComment(Long id, CommentDto commentDto, User user) {
        Optional<Comment> optionalCommnet = commentRepository.findById(id);
        Comment comment = optionalCommnet.orElseThrow(
                () -> new RequestException(ErrorCode.NULL_COMMENT_400)
        );

        if (comment.getCommentUsername().equals(user.getUsername())) {
            comment.update(commentDto);
            return new CommentDto(comment);
        } else if (user.getRole() == UserRoleEnum.ADMIN) {
            comment.update(commentDto);
            return new CommentDto(comment);
        } else {
            throw new RequestException(ErrorCode.NULL_USER_ACCESS_400);
        }
    }

    public CommentDto deleteComment(Long id, User user) {
        Optional<Comment> optionalCommnet = commentRepository.findById(id);
        Comment comment = optionalCommnet.orElseThrow(
                () -> new RequestException(ErrorCode.NULL_COMMENT_400)
        );

        if (comment.getCommentUsername().equals(user.getUsername())) {
            commentRepository.delete(comment);
            return new CommentDto(comment);
        } else if (user.getRole() == UserRoleEnum.ADMIN) {
            commentRepository.delete(comment);
            return new CommentDto(comment);
        } else {
            throw new RequestException(ErrorCode.NULL_USER_ACCESS_400);
        }
    }

    public void LikeComment(long id, User user) {
        Optional<Comment> optionalCommnet = commentRepository.findById(id);
        Comment comment = optionalCommnet.orElseThrow(
                () -> new RequestException(ErrorCode.NULL_COMMENT_400)
        );

        Optional<CommentLike> optionalCommentLike = commentLikeRepository.findByCommentAndUser(comment, user);

        if (!optionalCommentLike.isPresent()) {
            CommentLike commentLike = new CommentLike(comment, user);
            comment.commentLike(1);
            commentLikeRepository.save(commentLike);
        }
    }

    public void deleteLikeComment(long id, User user) {
        Optional<Comment> optionalCommnet = commentRepository.findById(id);
        Comment comment = optionalCommnet.orElseThrow(
                () -> new RequestException(ErrorCode.NULL_COMMENT_400)
        );

        Optional<CommentLike> optionalCommentLike = commentLikeRepository.findByCommentAndUser(comment, user);

//        if (optionalBoardLike.isPresent()) {
        CommentLike commentLike = new CommentLike(comment, user);
        comment.commentLike(-1);
        commentLikeRepository.delete(commentLike);
//        }
    }
}