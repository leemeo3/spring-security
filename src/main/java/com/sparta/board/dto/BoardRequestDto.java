package com.sparta.board.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BoardRequestDto {
    private Long id;
    private String title;
    private String name;
    private String contents;
    private String password;
}
