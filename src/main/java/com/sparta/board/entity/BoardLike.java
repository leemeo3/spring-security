package com.sparta.board.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "BoardLike")
public class BoardLike implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "boardLikeId")
    private Board board;

    @ManyToOne
    @JoinColumn(name = "userId", referencedColumnName = "username")
    private User user;

    public BoardLike(Board board, User user) {
        this.board      = board;
        this.user       = user;
    }
}