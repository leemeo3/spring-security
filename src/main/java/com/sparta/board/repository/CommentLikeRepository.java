package com.sparta.board.repository;

import com.sparta.board.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {
    Optional<CommentLike> findByCommentAndUser(Comment comment, User user);
    //    Optional<CommentLike> findByCommentAndUsername(Comment comment, User user);
//    Optional<CommentLike> deleteCommentLikeByAndUser(Comment comment, User user);
}
