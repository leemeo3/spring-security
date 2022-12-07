package com.sparta.board.service;

import com.sparta.board.dto.CommentDto;
import com.sparta.board.entity.Board;
import com.sparta.board.entity.Comment;
import com.sparta.board.entity.User;
import com.sparta.board.entity.UserRoleEnum;
import com.sparta.board.jwt.JwtUtil;
import com.sparta.board.repository.BoardRepository;
import com.sparta.board.repository.CommentRepository;
import com.sparta.board.repository.UserRepository;
import com.sparta.board.util.exception.ErrorCode;
import com.sparta.board.util.exception.RequestException;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public CommentDto addComment(CommentDto commentDto, Long id, HttpServletRequest request) {
        String token = jwtUtil.resolveToken(request);
        Claims claims;

        if (token != null) {
            // Token 검증
            if (jwtUtil.validateToken(token)) {
                // 토큰에서 사용자 정보 가져오기
                claims = jwtUtil.getUserInfoFromToken(token);
            } else {
                throw new RequestException(ErrorCode.BAD_TOKEN_400);
            }
            Optional<Board> optionalBoard = boardRepository.findById(id);
            Board board = optionalBoard.orElseThrow(
                    () -> new RequestException(ErrorCode.NULL_CONTENTS_400)
            );

            Comment comment = Comment.builder()
                    .commentId(commentDto.getCommentId())
                    .board(board)
                    .commentUsername(claims.getSubject())
                    .commentContents(commentDto.getCommentContents())
                    .build();

            return new CommentDto(commentRepository.save(comment));
        }
        throw new RequestException(ErrorCode.NULL_TOKEN_400);
    }

    @Transactional
    public CommentDto updateComment(CommentDto commentDto, Long id, HttpServletRequest request) {
        String token = jwtUtil.resolveToken(request);
        Claims claims;

        if (token != null) {
            // Token 검증
            if (jwtUtil.validateToken(token)) {
                // 토큰에서 사용자 정보 가져오기
                claims = jwtUtil.getUserInfoFromToken(token);
            } else {
                throw new RequestException(ErrorCode.BAD_TOKEN_400);
            }
            User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                    () -> new RequestException(ErrorCode.NULL_USER_400)
            );

            Optional<Comment> optionalCommnet = commentRepository.findById(id);
            Comment comment = optionalCommnet.orElseThrow(
                    () -> new RequestException(ErrorCode.NULL_COMMENT_400)
            );

            if (comment.getCommentUsername().equals(claims.getSubject())) {
                comment.update(commentDto);
                return new CommentDto(commentRepository.save(comment));
            } else if (user.getRole() == UserRoleEnum.ADMIN) {
                comment.update(commentDto);
                return new CommentDto(commentRepository.save(comment));
            }else {
                throw new RequestException(ErrorCode.NULL_USER_ACCESS_400);
            }
        }
        throw new RequestException(ErrorCode.NULL_TOKEN_400);
    }

    public CommentDto deleteComment(Long id, HttpServletRequest request) {
        String token = jwtUtil.resolveToken(request);
        Claims claims;

        if (token != null) {
            // Token 검증
            if (jwtUtil.validateToken(token)) {
                // 토큰에서 사용자 정보 가져오기
                claims = jwtUtil.getUserInfoFromToken(token);
            } else {
                throw new RequestException(ErrorCode.BAD_TOKEN_400);
            }
            User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                    () -> new RequestException(ErrorCode.NULL_USER_400)
            );

            Optional<Comment> optionalCommnet = commentRepository.findById(id);
            Comment comment = optionalCommnet.orElseThrow(
                    () -> new RequestException(ErrorCode.NULL_COMMENT_400)
            );

            if (comment.getCommentUsername().equals(claims.getSubject())) {
                commentRepository.delete(comment);
                return new CommentDto(comment);
            }else if (user.getRole() == UserRoleEnum.ADMIN) {
                commentRepository.delete(comment);
                return new CommentDto(comment);
            }else {
                throw new RequestException(ErrorCode.NULL_USER_ACCESS_400);
            }
        }
        throw new RequestException(ErrorCode.NULL_TOKEN_400);
    }
}
