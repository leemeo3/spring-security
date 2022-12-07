package com.sparta.board.service;

import com.sparta.board.dto.BoardRequestDto;
import com.sparta.board.dto.BoardResponseDto;
import com.sparta.board.dto.StatusResponseDto;
import com.sparta.board.entity.Board;
import com.sparta.board.entity.User;
import com.sparta.board.entity.UserRoleEnum;
import com.sparta.board.jwt.JwtUtil;
import com.sparta.board.repository.BoardRepository;
import com.sparta.board.repository.UserRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Transactional
    public BoardResponseDto create(BoardRequestDto requestDto, HttpServletRequest request) {
        // Request에서 Token 가져오기
        String token = jwtUtil.resolveToken(request);
        Claims claims;

        // 토큰이 있는 경우 토큰 검증 진행
        if (token != null) {
            // Token 검증
            if (jwtUtil.validateToken(token)) {
                // 토큰에서 사용자 정보 가져오기
                claims = jwtUtil.getUserInfoFromToken(token);
            } else {
                throw new IllegalArgumentException("유효하지 않은 토큰입니다");
            }
            User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                    () -> new IllegalArgumentException("사용자가 존재하지 않습니다.")
            );
            Board board = new Board(requestDto, user.getUsername());
            boardRepository.save(board);
            return new BoardResponseDto(board);
        // 토큰이 없는 경우 상태코드 출력
        }
        throw new IllegalArgumentException("토큰이 유효하지 않습니다");


        // 토큰 검증 진행 후 이상없을 시
    }

    public List<BoardResponseDto> getBoard() {
        List<Board> list = boardRepository.findAllByOrderByModifiedAtDesc();
        return list.stream().map(board -> new BoardResponseDto(board)).collect(Collectors.toList());
    }

    public BoardResponseDto getBoard(Long id) {
        Board board = boardRepository.findById(id).orElseThrow(
                () -> new RuntimeException("게시글을 조회 할 수 없습니다.")
        );
        return new BoardResponseDto(board);
    }

    @Transactional
    public BoardResponseDto updateBoard(long id, BoardRequestDto requestDto, HttpServletRequest request) {
        // Request에서 Token 가져오기
        String token = jwtUtil.resolveToken(request);

        Claims claims = jwtUtil.getUserInfoFromToken(token);
        User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                () -> new IllegalArgumentException("사용자가 존재하지 않습니다.")
        );
        if (user.getRole() == UserRoleEnum.ADMIN) {
            System.out.println("user.getRole() = " + user.getRole());
            Board board = boardRepository.findById(id).orElseThrow(
                    () -> new IllegalArgumentException("게시글이 존재하지 않습니다")
            );
            board.update(requestDto);
            return new BoardResponseDto(board);
        }

        // 토큰이 있는 경우 토큰 검증 진행
        if (token != null) {
            // Token 검증
            if (jwtUtil.validateToken(token)) {
                // 토큰에서 사용자 정보 가져오기
                claims = jwtUtil.getUserInfoFromToken(token);
            } else {
                throw new IllegalArgumentException("유효하지 않은 토큰입니다");
            }
            Board board = boardRepository.findById(id).orElseThrow(
                    () -> new IllegalArgumentException("게시글이 존재하지 않습니다")
            );
            if (claims.getSubject().equals(board.getUsername())) {
                board.update(requestDto);
                // 성공했을때
                return new BoardResponseDto(board);
            }else {
                throw new IllegalArgumentException("작성자만 수정 할 수 있습니다.");
            }
        // 토큰이 없는 경우 상태코드 출력
        }
        // 토큰 검증 후 이상없을 시
        // 해당 사용자가 작성한 게시글인지 검증
        throw new IllegalArgumentException("토큰이 유효하지 않습니다");
    }

    public BoardResponseDto deleteBoard(long id, HttpServletRequest request) {
        // Request에서 Token 가져오기
        String token = jwtUtil.resolveToken(request);
        Claims claims = jwtUtil.getUserInfoFromToken(token);
        User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                () -> new IllegalArgumentException("사용자가 존재하지 않습니다.")
        );
        if (user.getRole() == UserRoleEnum.ADMIN) {
            System.out.println("user.getRole() = " + user.getRole());
            Board board = boardRepository.findById(id).orElseThrow(
                    () -> new IllegalArgumentException("게시글이 존재하지 않습니다")
            );
            boardRepository.delete(board);
            return new BoardResponseDto(board);
        }

        // 토큰이 있는 경우 토큰 검증 진행
        if (token != null) {
            // Token 검증
            if (jwtUtil.validateToken(token)) {
                // 토큰에서 사용자 정보 가져오기
                claims = jwtUtil.getUserInfoFromToken(token);
            } else {
                throw new IllegalArgumentException("Token Error");
            }

            Board board = boardRepository.findById(id).orElseThrow(
                    () -> new IllegalArgumentException("게시글이 존재하지 않습니다")
            );
            if (claims.getSubject().equals(board.getUsername())) {
                boardRepository.delete(board);
                return new BoardResponseDto(board);
            }else {
                throw new IllegalArgumentException("작성자만 삭제 할 수 있습니다");
            }
        }
        // 토큰이 없는 경우 상태코드 출력
        throw new IllegalArgumentException("토큰이 유효하지 않습니다");
    }
}
