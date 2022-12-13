package com.sparta.board.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sparta.board.dto.CommentDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Comment extends Timestamped{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "boardCommentId")
    private Board board;

    @Column(nullable = false)
    private String commentContents;

    @Column(nullable = false)
    private String commentUsername;

    @Column(nullable = false)
    private int likesNum;


    public void update(CommentDto commentDto) {
        this.commentContents = commentDto.getCommentContents();
    }

    public void commentLike(int cnt) {
        this.likesNum = likesNum + cnt;
    }
}
