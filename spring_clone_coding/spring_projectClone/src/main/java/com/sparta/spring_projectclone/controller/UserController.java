package com.sparta.spring_projectclone.controller;

import com.sparta.spring_projectclone.dto.requestDto.LoginRequestDto;
import com.sparta.spring_projectclone.dto.requestDto.SignupRequestDto;
import com.sparta.spring_projectclone.jwt.JwtTokenProvider;
import com.sparta.spring_projectclone.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletResponse;


@RequiredArgsConstructor
@RestController
public class UserController {
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    // 회원 가입 요청 처리
    @PostMapping("api/user/signup")
    public ResponseEntity<String> registerUser(@RequestBody SignupRequestDto requestDto) {
        try {
            userService.registerUser(requestDto);
            return new ResponseEntity<>("회원가입 완료!!", HttpStatus.OK);
        }
        catch(IllegalStateException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // 로그인 요청 처리
    @PostMapping("api/user/login")
    public ResponseEntity<String> login(final HttpServletResponse response, @RequestBody LoginRequestDto loginRequestDto) {
        if (userService.login(loginRequestDto)) {
            String token = jwtTokenProvider.createToken(loginRequestDto.getUsername());
            System.out.println(token);
            response.addHeader("Authorization", token);
            return new ResponseEntity<>("로그인 성공!!", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("로그인 실패 : username 또는 password 를 확인해주세요.", HttpStatus.BAD_REQUEST);
        }
    }
}
