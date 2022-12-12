package com.sparta.board.repository;

import com.sparta.board.entity.Board;
import com.sparta.board.entity.BoardLike;
import com.sparta.board.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BoardLikeRepository extends JpaRepository<BoardLike, Long> {
//    Optional<BoardLike> findByBoard(Board board);
//
//    Optional<BoardLike> findByBoardAndUser(Board board, User user);
}
