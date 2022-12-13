package com.sparta.board.service;

import com.sparta.board.dto.BoardRequestDto;
import com.sparta.board.dto.BoardResponseDto;
import com.sparta.board.entity.*;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final BoardLikeRepository boardLikeRepository;

    @Transactional
    public BoardResponseDto create(BoardRequestDto requestDto, User user) {
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

    public void LikeBoard(long id, User user) {
        Board board = boardRepository.findById(id).orElseThrow(
                () -> new RequestException(ErrorCode.NULL_CONTENTS_400)
        );

        Optional<BoardLike> optionalBoardLike = boardLikeRepository.findByBoardAndUser(board, user);

        if (!optionalBoardLike.isPresent()) {
            BoardLike boardLike = new BoardLike(board, user);
            board.boardLike(1);
            boardLikeRepository.save(boardLike);
        }
    }

    public void deleteLikeBoard(long id, User user) {
        Board board = boardRepository.findById(id).orElseThrow(
                () -> new RequestException(ErrorCode.NULL_CONTENTS_400)
        );

        Optional<BoardLike> optionalBoardLike = boardLikeRepository.findByBoardAndUser(board, user);

//        if (optionalBoardLike.isPresent()) {
            BoardLike boardLike = new BoardLike(board, user);
            board.boardLike(-1);
            boardLikeRepository.delete(boardLike);
//        }
    }
}
