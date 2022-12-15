package com.sparta.board.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sparta.board.dto.CommentDto;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Comment extends Timestamped{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "boardId")
    private Board board;

    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL) // comment가 지워질시 commentLike가 전부 지워진다.
    @OrderBy("id desc")
    private List<CommentLike> commentLikes = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "user_Id")
    private User user;

    @Column(nullable = false)
    private String commentContents;

    @Column(nullable = false)
    private String commentUsername;

    @Column(nullable = false)
    private int likesNum;

    public Comment(CommentDto commentDto, Board board, User user) {
        this.commentId          = commentDto.getCommentId();
        this.commentContents    = commentDto.getCommentContents();
        this.commentUsername    = user.getUsername();
        this.board              = board;
        this.user               = user;
        this.commentLikes       = getCommentLikes();
        this.likesNum           = getLikesNum();
    }

    public void update(CommentDto commentDto) {
        this.commentContents = commentDto.getCommentContents();
    }

    public void commentLike(int cnt) {
        this.likesNum = likesNum + cnt;
    }
}
