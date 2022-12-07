package com.sparta.board.controller;

import com.sparta.board.dto.*;
import com.sparta.board.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    @GetMapping("/signup")
    public ModelAndView signupPage() {
        return new ModelAndView("signup");
    }

    @GetMapping("/login")
    public ModelAndView loginPage() {
        return new ModelAndView("login");
    }
    // GET 값을 받는다 조회
    // POST 값을 보낸다 등록
    // PUT 값을 변경한다 변경
    // DELETE 값을 삭제한다 삭제
    // Service -> repository  entity
    // 나머지는 dto
    // controller dto->dto service -> repository -> entity -> DB
    // data transfer object

    @ResponseBody
    @PostMapping("/signup")
    public ResponseEntity<ResponseMsgDto> signup(@RequestBody @Valid SignupRequestDto signupRequestDto) {
        userService.signup(signupRequestDto);
        return ResponseEntity.ok(new ResponseMsgDto(HttpStatus.OK,"가입완료"));
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseMsgDto> login(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response) {
        userService.login(loginRequestDto, response);
        return ResponseEntity.ok(new ResponseMsgDto(HttpStatus.OK,"로그인 성공"));
    }
}