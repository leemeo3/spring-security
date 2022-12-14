package com.sparta.board.service;

import com.sparta.board.dto.*;
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
    // 의존성 주입
    private final BoardRepository boardRepository;
    private final BoardLikeRepository boardLikeRepository;

    // 게시글 생성
    public BoardResponseDto create(BoardRequestDto requestDto, User user) {
        // board 생성자를 통해 dto -> entity 변환
        Board board = new Board(requestDto, user.getUsername());
        // DB에 entity 저장
        boardRepository.save(board);
        // entity -> Dto 반환
        return new BoardResponseDto(board);
    }

    // 게시글 조회
    public List<BoardResponseDto> getBoard() {
        List<Board> list = boardRepository.findAllByOrderByModifiedAtDesc();
        return list.stream().map(board -> new BoardResponseDto(board)).collect(Collectors.toList());
    }

    // 게시글 선택 조회
    public BoardResponseDto getBoard(Long id) {
        Board board = boardRepository.findById(id).orElseThrow(
                () -> new RequestException(ErrorCode.NULL_CONTENTS_400)
        );
        return new BoardResponseDto(board);
    }

    // 게시글 수정
    @Transactional
    public BoardResponseDto updateBoard(long id, BoardRequestDto requestDto, User user) {
        Board board = boardRepository.findById(id).orElseThrow(         // id를 통해 db에서 수정할 게시글을 찾는다. 찾지 못할경우 Error
                () -> new RequestException(ErrorCode.NULL_CONTENTS_400)
        );

        if (user.getUsername().equals(board.getUsername())) {           // 토큰인증된 user의 아이디와 찾은 게시글의 아이디가 맞는지 비교한다.
            board.update(requestDto);                                   // 맞을경우 Dto -> entity 해주면서 수정
            return new BoardResponseDto(board);                         // entity -> dto 반환값 출력
        } else if (user.getRole() == UserRoleEnum.ADMIN) {              // 검증은 맞지 않지만 ADMIN일 경우
            board.update(requestDto);                                   // 맞을경우 Dto -> entity
            return new BoardResponseDto(board);                         // entity -> dto 반환값 출력
        } else {
            throw new RequestException(ErrorCode.NULL_USER_ACCESS_400); // 아니라면 error 반환
        }
    }

    // 게시글 삭제
    public BoardResponseDto deleteBoard(long id, User user) {
        Board board = boardRepository.findById(id).orElseThrow(         // id를 통해 db에서 삭제할 게시글을 찾는다. 찾지 못할 경우 Error
                () -> new RequestException(ErrorCode.NULL_CONTENTS_400)
        );

        if (user.getUsername().equals(board.getUsername())) {           // 토큰인증된 user의 아이디와 찾은 게시글의 아이디가 맞는지 비교한다.
            boardRepository.delete(board);                              // 맞을경우 삭제 진행
            return new BoardResponseDto(board);                         // entity -> Dto 반환값 출력
        } else if (user.getRole() == UserRoleEnum.ADMIN) {              // 검증이 맞지 않지만 ADMIN 일 경우
            boardRepository.delete(board);                              // 맞을경우 삭제 진행
            return new BoardResponseDto(board);                         // entity -> Dto 반환값 출력
        } else {
            throw new RequestException(ErrorCode.NULL_USER_ACCESS_400); // 아니라면 error 반환
        }
    }

    // 게시글 좋아요
    public void LikeBoard(long id, User user) {
        Board board = boardRepository.findById(id).orElseThrow(             //id를 통해 좋아요할 게시글을 찾는다 찾지못할경우 Error
                () -> new RequestException(ErrorCode.NULL_CONTENTS_400)
        );

        Optional<BoardLike> optionalBoardLike = boardLikeRepository.findByBoardAndUser(board, user);
                                                                            // JPA를 통해 게시글과 user 정보가 일치한 데이터 OptionaList담기
        if (!optionalBoardLike.isPresent()) {                               // 리스트안에 내용물이 있을 경우
            BoardLike boardLike = new BoardLike(board, user);               // 생성자안에 연관관계 값의 데이터 최신화를 위해 보냄
            board.boardLike(1);                                        // 좋아요 이므로 1 추가
            boardLikeRepository.save(boardLike);                            // db에 저장
        } else {
            throw new RequestException(ErrorCode.NULL_CONTENTS_400);        // 아닐 경우 error 반환
        }
    }

    // 게시글 좋아요 취소
    public void deleteLikeBoard(long id, User user) {
        Board board = boardRepository.findById(id).orElseThrow(             //id를 통해 좋아요 취소 할 게시글을 찾는다 찾지못할경우 Error
                () -> new RequestException(ErrorCode.NULL_CONTENTS_400)
        );

        Optional<BoardLike> optionalBoardLike = boardLikeRepository.findByBoardAndUser(board, user);
                                                                            // JPA를 통해 게시글과 user 정보가 일치한 데이터 OptionaList담기
        if (optionalBoardLike.isPresent()) {                                // 리스트안에 내용물이 있을 경우
            board.boardLike(-1);                                       // 좋아요 취소 이므로 -1
            boardLikeRepository.deleteByBoardAndUser(board, user);          // JPA를 통해 삭제 진행
        }else {
            throw new RequestException(ErrorCode.NULL_CONTENTS_400);        // 아닐 경우 error 반환
        }
    }
}
