package com.sparta.board.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;


@Getter
@NoArgsConstructor
public class ResponseMsgDto {
    private HttpStatus statusCode;
    private String msg;

    public ResponseMsgDto(HttpStatus statusCode, String msg) {
        this.statusCode = statusCode;
        this.msg = msg;
    }
}