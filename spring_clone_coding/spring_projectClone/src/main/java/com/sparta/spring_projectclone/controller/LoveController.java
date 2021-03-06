package com.sparta.spring_projectclone.controller;

import com.sparta.spring_projectclone.security.UserDetailsImpl;
import com.sparta.spring_projectclone.service.LoveService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LoveController {

    private final LoveService loveService;

    //상세페이지의 좋아요(찜)
    @PostMapping("/api/love/{postId}")
    public void loves(@PathVariable Long postId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        loveService.loves(postId, userDetails);
    }
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(e.getMessage());
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<String> handleNullPointerException(NullPointerException e) {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body("로그인 해주세요");
    }
}
