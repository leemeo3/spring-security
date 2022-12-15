package com.sparta.board.service;

import com.sparta.board.dto.CommentDto;
import com.sparta.board.dto.CommentRequestDto;
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
    // 의존성 주입
    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;
    private final CommentLikeRepository commentLikeRepository;

    // 댓글 추가
    public CommentDto addComment(CommentDto commentDto, Long id, User user) {
        Board board = boardRepository.findById(id).orElseThrow(             // 댓글을 추가할 게시글을 찾는다.
                () -> new RequestException(ErrorCode.NULL_CONTENTS_400)
        );
        Comment comment = Comment.builder()                                 // comment entity에 빌더적용
                .commentId(commentDto.getCommentId())                       // id 테이블
                .board(board)                                               // Board ID
                .commentUsername(user.getUsername())                        // 유저아이디
                .commentContents(commentDto.getCommentContents())           // 댓글 내용
                .build();

        return new CommentDto(commentRepository.save(comment));             // comment entity의 내용을 저장
    }

    // 댓글 수정
    @Transactional
    public CommentDto updateComment(Long id, CommentDto commentDto, User user) {
        Comment comment = commentRepository.findById(id).orElseThrow(       // 댓글을 수정할 게시글을 찾는다.
                () -> new RequestException(ErrorCode.NULL_COMMENT_400)
        );

        if (comment.getCommentUsername().equals(user.getUsername())) {      // 토큰인증한 유저아이디와 게시글의 유저아이디 비교
            comment.update(commentDto);                                     // 맞을 경우 Dto - entity하며 수정 진행
            return new CommentDto(comment);                                 // entity -> Dto 반환
        } else if (user.getRole() == UserRoleEnum.ADMIN) {                  // 관리자일 경우
            comment.update(commentDto);                                     // 맞을 경우 Dto - entity하며 수정 진행
            return new CommentDto(comment);                                 // entity -> Dto 반환
        } else {
            throw new RequestException(ErrorCode.NULL_USER_ACCESS_400);     // 아닐 경우 error 반환
        }
    }

    // 댓글 삭제
    public CommentDto deleteComment(Long id, User user) {
        Comment comment = commentRepository.findById(id).orElseThrow(       // 댓글을 삭제할 게시글을 찾는다
                () -> new RequestException(ErrorCode.NULL_COMMENT_400)
        );

        if (comment.getCommentUsername().equals(user.getUsername())) {      // 토큰인증한 유저아이디와 게시글의 유저아이디 비교
            commentRepository.delete(comment);                              // 맞을 경우 삭제 진행
            return new CommentDto(comment);                                 // entity -> Dto 반환
        } else if (user.getRole() == UserRoleEnum.ADMIN) {                  // 관리자일 경우
            commentRepository.delete(comment);                              // 맞을 경우 삭제 진행
            return new CommentDto(comment);                                 // entity -> Dto 반환
        } else {
            throw new RequestException(ErrorCode.NULL_USER_ACCESS_400);     // 아닐 경우 error 반환
        }
    }

    // 댓글 좋아요
    public void LikeComment(long id, User user) {
        Comment comment = commentRepository.findById(id).orElseThrow(       // 댓글을 좋아요할 게시글을 찾는다.
                () -> new RequestException(ErrorCode.NULL_COMMENT_400)
        );

        Optional<CommentLike> optionalCommentLike = commentLikeRepository.findByCommentAndUser(comment, user);
                                                                            // JPA를 통해 게시글과 user 정보가 일치한 데이터 OptionaList담기
        if (!optionalCommentLike.isPresent()) {                             // Optional 리스트가 비어 있을 경우
            CommentLike commentLike = new CommentLike(comment, user);       // 생성자안에 연관관계 값의 데이터 최신화를 위해 보냄
            comment.commentLike(1);                                    // 좋아요 갯수 1 추가
            commentLikeRepository.save(commentLike);                        // 저장 진행
        } else {
            throw new RequestException(ErrorCode.NULL_LIKE_400);         // 아닐 경우 Error 반환
        }
    }

    // 댓글 취소
    public void deleteLikeComment(long id, User user) {
        Comment comment = commentRepository.findById(id).orElseThrow(       // 댓글을 좋아요를 취소할 게시글을 찾는다.
                () -> new RequestException(ErrorCode.NULL_COMMENT_400)
        );

        Optional<CommentLike> optionalCommentLike = commentLikeRepository.findByCommentAndUser(comment, user);
                                                                            // JPA를 통해 게시글과 user 정보가 일치한 데이터 OptionaList담기
        if (optionalCommentLike.isPresent()) {                              // Optional 리스트가 비어 있지 않을 경우
        comment.commentLike(-1);                                       // 좋아요 갯수 1 빼기
        commentLikeRepository.deleteByCommentAndUser(comment, user);        // JPA를 통해 게시글과 user 정보 일치한 데이터 삭제
        } else {
            throw new RequestException(ErrorCode.NULL_LIKE_400);         // 아닐 경우 Error 반환
        }
    }
}