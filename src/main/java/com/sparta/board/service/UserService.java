package com.sparta.board.service;

import com.sparta.board.dto.LoginRequestDto;
import com.sparta.board.dto.SignupRequestDto;
import com.sparta.board.entity.User;
import com.sparta.board.entity.UserRoleEnum;
import com.sparta.board.jwt.JwtUtil;
import com.sparta.board.repository.UserRepository;
import com.sparta.board.util.exception.ErrorCode;
import com.sparta.board.util.exception.RequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    // 의존성 주입
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private static final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";
    private final PasswordEncoder passwordEncoder;

    // 회원가입
    @Transactional
    public void signup(SignupRequestDto signupRequestDto) {
        String username = signupRequestDto.getUsername();                           // Dto -> entity
        String password = passwordEncoder.encode(signupRequestDto.getPassword());   // Dto -> entity

        // 회원 중복 확인
        Optional<User> found = userRepository.findByUsername(username);             // userRepository에서 유저 찾기
        if (found.isPresent()) {                                                    // 유저가 있을 경우 중복 error
            throw new RequestException(ErrorCode.USER_OVERLAP_400);
        }

        // 사용자 ROLE 확인
        UserRoleEnum role = UserRoleEnum.USER;
        if (signupRequestDto.isAdmin()) {
            if (!signupRequestDto.getAdminToken().equals(ADMIN_TOKEN)) {
                throw new RequestException(ErrorCode.NULL_ADMIN_PASSWORD_400);
            }
            role = UserRoleEnum.ADMIN;
        }

        User user = new User(username, password, role);
        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public void login(LoginRequestDto loginRequestDto, HttpServletResponse response) {
        String username = loginRequestDto.getUsername();
        String password = loginRequestDto.getPassword();

        // 사용자 확인
        User user = userRepository.findByUsername(username).orElseThrow(            // 유저 찾기
                () -> new RequestException(ErrorCode.NULL_USER_400)                 // 없을 경우 유저없음 error
        );

        // 비밀번호 확인
        if(!passwordEncoder.matches(password, user.getPassword())) {
            throw new RequestException(ErrorCode.PASSWORD_NOT_400);
        }

        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(user.getUsername(), user.getRole()));
    }
}