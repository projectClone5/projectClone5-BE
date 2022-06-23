package com.sparta.spring_projectclone.controller;

import com.sparta.spring_projectclone.dto.requestDto.PostRequestDto;
import com.sparta.spring_projectclone.dto.responseDto.PostResponseDto;
import com.sparta.spring_projectclone.security.UserDetailsImpl;
import com.sparta.spring_projectclone.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    //전체 포스트
    @GetMapping("/api/posts")
    public List<PostResponseDto> getPost() {
        return postService.getPost();
    }

    //포스트 상세페이지
    @GetMapping("/api/post/{postId}")
    public PostResponseDto getPostDetail(@PathVariable Long postId) {
        return postService.getPostDetail(postId);
    }

    //포스트 작성
    @PostMapping("/api/posts")
    public void savePost(@ModelAttribute PostRequestDto postRequestDto,
                         @AuthenticationPrincipal UserDetailsImpl userDetails) {
        postService.savePost(postRequestDto, userDetails);
    }

    //포스트 수정
    @PutMapping("/api/post/{postId}")
    public void updatePost(@PathVariable Long postId,
                           @ModelAttribute PostRequestDto postRequestDto,
                           @AuthenticationPrincipal UserDetailsImpl userDetails) {
        postService.updatePost(postId, postRequestDto, userDetails);
    }

    //포스트 삭제
    @DeleteMapping("/api/post/{postId}")
    public void deletePost(@PathVariable Long postId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        postService.deletePost(postId, userDetails);
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
