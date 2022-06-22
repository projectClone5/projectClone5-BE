package com.sparta.spring_projectclone.controller;

import com.sparta.spring_projectclone.dto.requestDto.LoginRequestDto;
import com.sparta.spring_projectclone.dto.requestDto.SignupRequestDto;

import com.sparta.spring_projectclone.dto.requestDto.UserRequestDto;
import com.sparta.spring_projectclone.exception.ApiResponseMessage;
import com.sparta.spring_projectclone.model.User;
import com.sparta.spring_projectclone.repository.UserRepository;
import com.sparta.spring_projectclone.security.EmailServiceImpl;
import com.sparta.spring_projectclone.security.UserDetailsImpl;
import com.sparta.spring_projectclone.jwt.JwtTokenProvider;
import com.sparta.spring_projectclone.service.EmailService;
import com.sparta.spring_projectclone.service.S3Uploader;
import com.sparta.spring_projectclone.service.UserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;


@RequiredArgsConstructor
@RestController
public class UserController {
    private final UserService userService;

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final S3Uploader s3Uploader;
    private final EmailService emailService;


    // 회원 가입 요청 처리
    @PostMapping("/api/user/signup")
    public ResponseEntity<String> registerUser(@RequestBody SignupRequestDto requestDto) {
        try {
            userService.registerUser(requestDto);
            System.out.println("1성공");
            return new ResponseEntity<>("회원가입 완료!!", HttpStatus.OK);
        } catch (IllegalStateException e) {
            System.out.println("1실패");
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // 로그인 요청 처리
    @PostMapping("/api/user/login")
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


    // 회원 조회
    @GetMapping("/api/user/{userId}")
    public List<User> userDetail(@PathVariable("userId") Long userId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        String username = userDetails.getUsername();
        System.out.println("username = " + username);
        if (userId.equals(userDetails.getUser().getId())){
//        return userDetails.getUsername(username);
            System.out.println("그럼 여기는?");
            return userRepository.findAllById(userId);
    } else {
            System.out.println("들어옴?");
            throw new IllegalArgumentException("로그인해주세요");
    }
//        userDetails.getUsername();
//        return new UserResponseDto( userDetails.getUsername(userId));
    }

    // 회원 정보 수정
    @PutMapping("/api/user/{userId}")
    public ResponseEntity<ApiResponseMessage> userUpdate(@PathVariable("userId") Long userId,
                                                         @RequestPart(value = "userImgUrl",required = false) MultipartFile multipartFile,
                                                         @RequestParam("nickname") String nickname,
                                                         @AuthenticationPrincipal UserDetailsImpl userDetails) {
        UserRequestDto userRequestDto = new UserRequestDto(nickname);
        userService.update(userId, userRequestDto, userDetails.getUsername(), multipartFile);
        ApiResponseMessage message = new ApiResponseMessage("Success", "개인정보가 수정 되었습니다.", "", "");
        return new ResponseEntity<ApiResponseMessage>(message, HttpStatus.OK);
    }

    @PostMapping("/api/user/emailConfirmation")
    @ApiOperation(value = "회원 가입시 이메인 인증", notes = "기존사용하고 있는 이메일을 통해 인증")
    @ApiResponses({
            @ApiResponse(code = 200, message = "성공"),
            @ApiResponse(code = 401, message = "인증 실패"),
            @ApiResponse(code = 404, message = "사용자 없음"),
            @ApiResponse(code = 500, message = "서버 오류")
    })
    public ResponseEntity<String> emailConfirm(
            @RequestBody @ApiParam(value="이메일정보 정보", required = true) String email) throws Exception {

        String confirm = emailService.sendSimpleMessage(email);

        return ResponseEntity.status(200).body(confirm);
    }

}
