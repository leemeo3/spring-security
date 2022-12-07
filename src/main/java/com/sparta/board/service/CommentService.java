package com.sparta.board.service;

import com.sparta.board.dto.BoardResponseDto;
import com.sparta.board.dto.CommentDto;
import com.sparta.board.entity.Board;
import com.sparta.board.entity.Comment;
import com.sparta.board.entity.User;
import com.sparta.board.jwt.JwtUtil;
import com.sparta.board.repository.BoardRepository;
import com.sparta.board.repository.CommentRepository;
import com.sparta.board.repository.UserRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
                throw new IllegalArgumentException("유효하지 않은 토큰입니다");
            }
            Optional<Board> optionalBoard = boardRepository.findById(id);
            Board board = optionalBoard.orElseThrow(
                    () -> new IllegalArgumentException("게시글이 존재하지 않습니다.")
            );

            Comment comment = Comment.builder()
                    .commentId(commentDto.getCommentId())
                    .board(board)
                    .commentUsername(claims.getSubject())
                    .commentContents(commentDto.getCommentContents())
                    .build();

            return new CommentDto(commentRepository.save(comment));
        }
        throw new IllegalArgumentException("토큰이 존재하지 않습니다");
    }

    public CommentDto updateComment(CommentDto commentDto, Long id, HttpServletRequest request) {
        String token = jwtUtil.resolveToken(request);
        Claims claims;

        if (token != null) {
            // Token 검증
            if (jwtUtil.validateToken(token)) {
                // 토큰에서 사용자 정보 가져오기
                claims = jwtUtil.getUserInfoFromToken(token);
            } else {
                throw new IllegalArgumentException("유효하지 않은 토큰입니다");
            }
            Optional<Comment> optionalCommnet = commentRepository.findById(id);
            Comment comment = optionalCommnet.orElseThrow(
                    () -> new IllegalArgumentException("댓글이 존재하지 않습니다.")
            );

            if (comment.getCommentUsername().equals(claims.getSubject())) {
                 comment.update(commentDto);
            } else {
                throw new IllegalArgumentException("댓글 작성자만 수정 할 수 있습니다.");
            }
            return new CommentDto(commentRepository.save(comment));
        }
        throw new IllegalArgumentException("토큰이 존재하지 않습니다.");
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
                throw new IllegalArgumentException("유효하지 않은 토큰입니다");
            }
            Optional<Comment> optionalCommnet = commentRepository.findById(id);
            Comment comment = optionalCommnet.orElseThrow(
                    () -> new IllegalArgumentException("댓글이 존재하지 않습니다.")
            );

            if (comment.getCommentUsername().equals(claims.getSubject())) {
                commentRepository.delete(comment);
                return new CommentDto(comment);
            }else {
                throw new IllegalArgumentException("댓글 작성자만 삭제 할 수 있습니다");
            }
        }
        throw new IllegalArgumentException("토큰이 존재하지 않습니다.");

    }
}
