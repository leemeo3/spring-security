package com.sparta.board.repository;

import com.sparta.board.entity.Board;
import com.sparta.board.entity.BoardLike;
import com.sparta.board.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface BoardLikeRepository extends JpaRepository<BoardLike, Long> {
    Optional<BoardLike> findByBoardAndUser(Board board, User user);
//    Optional<BoardLike> findByBoardAndUsername(Board board, User user);
//    Optional<BoardLike> deleteBoardLikeByBoardAndUser(Board board, User user);
    @Transactional
    void deleteByBoardAndUser(Board board, User user);
}
