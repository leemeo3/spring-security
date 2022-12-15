package com.sparta.board.entity;

import com.sparta.board.dto.BoardRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "BOARD")
public class Board extends Timestamped implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String contents;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private int likesNum;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false, referencedColumnName = "username")
    private User user;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)       // board가 지워질시 comment가 지워진다
    @OrderBy("createdAt DESC")

    private List<Comment> comment = new ArrayList<>();

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)       // board가 지워질시 boardLike가 지워진다
    @OrderBy("id desc")

    private List<BoardLike> boardLikes = new ArrayList<>();


    public Board(BoardRequestDto requestDto, User user, String username) {
        // Dto -> Entity
        this.title      = requestDto.getTitle();        // 제목
        this.name       = requestDto.getName();         // 아이디
        this.contents   = requestDto.getContents();     // 내용
        this.password   = requestDto.getPassword();     // 비밀번호
        this.user       = user;                          // 유저아이디
        this.likesNum   = getLikesNum();                // 좋아요 갯수
        this.username   = username;
    }

    public void update(BoardRequestDto requestDto) {
        // Dto -> Entity
        this.title      = requestDto.getTitle();        // 제목
        this.name       = requestDto.getName();         // 아이디
        this.contents   = requestDto.getContents();     // 내용
    }

    public void boardLike(int cnt) {
        this.likesNum = likesNum + cnt;
    }
}