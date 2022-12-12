package com.sparta.board.service;

import com.sparta.board.dto.BoardRequestDto;
import com.sparta.board.dto.BoardResponseDto;
import com.sparta.board.entity.Board;
import com.sparta.board.entity.BoardLike;
import com.sparta.board.entity.User;
import com.sparta.board.entity.UserRoleEnum;
import com.sparta.board.jwt.JwtUtil;
import com.sparta.board.repository.BoardLikeRepository;
import com.sparta.board.repository.BoardRepository;
import com.sparta.board.repository.UserRepository;
import com.sparta.board.util.exception.ErrorCode;
import com.sparta.board.util.exception.RequestException;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final BoardLikeRepository boardLikeRepository;

    @Transactional
    public BoardResponseDto create(BoardRequestDto requestDto, User user) {

            user = userRepository.findByUsername(user.getUsername()).orElseThrow(
                    () -> new RequestException(ErrorCode.NULL_USER_400)
            );
            Board board = new Board(requestDto, user.getUsername());
            boardRepository.save(board);
            return new BoardResponseDto(board);
    }

    public List<BoardResponseDto> getBoard() {
        List<Board> list = boardRepository.findAllByOrderByModifiedAtDesc();
        return list.stream().map(board -> new BoardResponseDto(board)).collect(Collectors.toList());
    }

    public BoardResponseDto getBoard(Long id) {
        Board board = boardRepository.findById(id).orElseThrow(
                () -> new RequestException(ErrorCode.NULL_CONTENTS_400)
        );
        return new BoardResponseDto(board);
    }

    @Transactional
    public BoardResponseDto updateBoard(long id, BoardRequestDto requestDto, User user) {
        user = userRepository.findByUsername(user.getUsername()).orElseThrow(
                () -> new RequestException(ErrorCode.NULL_USER_400)
        );
        Board board = boardRepository.findById(id).orElseThrow(
                () -> new RequestException(ErrorCode.NULL_CONTENTS_400)
        );
        if (user.getUsername().equals(board.getUsername())) {
            board.update(requestDto);
            return new BoardResponseDto(board);
        } else if (user.getRole() == UserRoleEnum.ADMIN) {
            board.update(requestDto);
            return new BoardResponseDto(board);
        } else {
            throw new RequestException(ErrorCode.NULL_USER_ACCESS_400);
        }
    }

    public BoardResponseDto deleteBoard(long id, User user) {
        user = userRepository.findByUsername(user.getUsername()).orElseThrow(
                () -> new RequestException(ErrorCode.NULL_USER_400)
        );

        Board board = boardRepository.findById(id).orElseThrow(
                () -> new RequestException(ErrorCode.NULL_CONTENTS_400)
        );

        if (user.getUsername().equals(board.getUsername())) {
            boardRepository.delete(board);
            return new BoardResponseDto(board);
        } else if (user.getRole() == UserRoleEnum.ADMIN) {
            boardRepository.delete(board);
            return new BoardResponseDto(board);
        } else {
            throw new RequestException(ErrorCode.NULL_USER_ACCESS_400);
        }
    }


//
//    public BoardResponseDto LikeBoard(long id, HttpServletRequest request) {
//        String token = jwtUtil.resolveToken(request);
//        Claims claims;
//
//        if (token != null) {
//            // Token 검증
//            if (jwtUtil.validateToken(token)) {
//                // 토큰에서 사용자 정보 가져오기
//                claims = jwtUtil.getUserInfoFromToken(token);
//            } else {
//                throw new RequestException(ErrorCode.BAD_TOKEN_400);
//            }
//
//            Board board = boardRepository.findById(id).orElseThrow(
//                    () -> new RequestException(ErrorCode.NULL_CONTENTS_400)
//            );
//            User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
//                    () -> new IllegalArgumentException("사용자가 존재하지 않습니다.")
//            );
//            Optional<BoardLike> optionalBoardLike = boardLikeRepository.findByBoardAndUser(board, user);
//
//            if (optionalBoardLike.isEmpty()) {
//                BoardLike boardLike = new BoardLike(id, claims.getSubject());
//                boardLikeRepository.save(boardLike);
//            } else {
//                System.out.println("테스트");
//            }
//        }
//        // 토큰이 없는 경우 상태코드 출력
//        throw new RequestException(ErrorCode.NULL_TOKEN_400);
//    }
}
