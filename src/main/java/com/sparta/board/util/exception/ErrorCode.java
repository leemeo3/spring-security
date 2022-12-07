package com.sparta.board.util.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    /*
     * 4xx 클라이언트 오류
     * 400 Bad Request : 잘못된 요청.
     * 401 Unauthorized : 인증이 필요함.
     * 403 Forbidden : 접근 권한 없음.
     * 404 Not Found : Resource 없음.
     * 405 Methods Not Allowed : 유효하지 않은 요청
     * 409 Conflict : 리소스 충돌(중복)
     *
     * 5xx 서버 오류
     * 500 Internal Server Error : 서버 오류 발생
     * 503 Service Unavailable : 서비스 사용 불가
     */
    //토큰 Error
    BAD_TOKEN_400(HttpStatus.BAD_REQUEST, "토큰이 유효하지 않습니다."),
    NULL_TOKEN_400(HttpStatus.BAD_REQUEST, "토큰이 존재하지 않습니다."),
    //사용자 Error
    NULL_USER_400(HttpStatus.BAD_REQUEST, "사용자가 존재하지 않습니다."),
    //접근 Error
    COMMON_BAD_REQUEST_400(HttpStatus.BAD_REQUEST, "잘못된 요청입니다.");


    private final HttpStatus httpStatus;
    private final String message;

}