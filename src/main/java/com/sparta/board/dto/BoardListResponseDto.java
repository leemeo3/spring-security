package com.sparta.board.dto;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class BoardListResponseDto {

    List<BoardDto> boradList = new ArrayList<>();

    public void addBoard(BoardDto boardToDto) {
        boradList.add(boardToDto);
    }
}
