package com.sparta.spring_projectclone.controller;

import com.sparta.spring_projectclone.dto.requestDto.LoginRequestDto;
import com.sparta.spring_projectclone.dto.requestDto.SignupRequestDto;

import com.sparta.spring_projectclone.dto.requestDto.UserRequestDto;
import com.sparta.spring_projectclone.dto.responseDto.UserResponseDto;
import com.sparta.spring_projectclone.exception.ApiResponseMessage;
import com.sparta.spring_projectclone.repository.UserRepository;
import com.sparta.spring_projectclone.security.UserDetailsImpl;
import com.sparta.spring_projectclone.jwt.JwtTokenProvider;
import com.sparta.spring_projectclone.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
public class UserController {
    private final UserService userService;
    private final UserRepository userRepository;

    // 회원 가입 요청 처리
    @PostMapping("/api/user/signup")
    public ResponseEntity<String> registerUser(@RequestBody SignupRequestDto requestDto) {
        try {
            userService.registerUser(requestDto);
            System.out.println("1성공");
            return new ResponseEntity<>("회원가입 완료!!", HttpStatus.OK);
        }
        catch(IllegalStateException e) {
            System.out.println("1실패");
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // 로그인 요청 처리
    @PostMapping("/api/user/login")
    public ResponseEntity<String> login(@RequestBody LoginRequestDto loginRequestDto) {
        if (userService.login(loginRequestDto)) {
//            String token = jwtTokenProvider.createToken(loginRequestDto.getUsername());
//            System.out.println(token);
            return new ResponseEntity<>("로그인 성공!!", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("로그인 실패 : username 또는 password 를 확인해주세요.", HttpStatus.BAD_REQUEST);
        }
    }


    // 회원 조회
    @GetMapping("/api/user/{userId}")
    public UserResponseDto userDetail(@PathVariable("userId") Long userId){
        return userRepository.findAllById(userId);
//        userDetails.getUsername();
//        return new UserResponseDto( userDetails.getUsername(userId));
    }

    // 회원 정보 수정
    @PutMapping("/api/user/{userId}")
    public ResponseEntity<ApiResponseMessage> userUpdate( @PathVariable("userId") Long userId, @RequestBody UserRequestDto userRequestDto , @AuthenticationPrincipal UserDetailsImpl userDetails){
        userService.update(userId, userRequestDto, userDetails.getUsername());
        ApiResponseMessage message = new ApiResponseMessage("Success", "개인정보가 수정 되었습니다.", "", "");
        return new  ResponseEntity<ApiResponseMessage>(message, HttpStatus.OK);
    }


}
