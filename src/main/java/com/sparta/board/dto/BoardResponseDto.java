package com.sparta.board.dto;

import com.sparta.board.entity.Board;
import com.sparta.board.entity.Comment;
import lombok.Getter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class BoardResponseDto {
    //응답 DTO

    private Long            id;             // ID
    private String          title;          // 제목
    private String          name;           // 아이디
    private String          contents;       // 내용
    private LocalDateTime   createdAt;      // 생성시간
    private LocalDateTime   modifiedAt;     // 변경시간
    private List<Comment> comments = new ArrayList<>();   // 게시글의 댓글

    public BoardResponseDto(Board board) {
        // 게시글 응답 DTO
        // entity -> DTO
        this.id         = board.getId();        // ID
        this.title      = board.getTitle();     // 제목
        this.name       = board.getName();      // 아이디
        this.contents   = board.getContents();  // 내용
        this.createdAt  = board.getCreatedAt(); // 작성시간
        this.modifiedAt = board.getModifiedAt();// 변경시간
        this.comments   = board.getComment();   // 댓글
    }

}
