package com.sparta.board.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BoardRequestDto {
    // 요청 DTO

    private Long    id;         // ID
    private String  title;      // 제목
    private String  name;       // 아이디
    private String  contents;   // 내용
    private String  password;   // 비밀번호
}
