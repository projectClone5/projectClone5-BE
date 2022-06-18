package com.sparta.spring_projectclone.controller;

import com.sparta.spring_projectclone.dto.requestDto.PostRequestDto;
import com.sparta.spring_projectclone.dto.responseDto.PostResponseDto;
import com.sparta.spring_projectclone.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    public void savePost(@RequestBody PostRequestDto requestDto,
                         @RequestPart MultipartFile multipartFile) {
        postService.savePost(requestDto);
    }

    //포스트 수정
    @PutMapping("/api/post/{postId}")
    public void updatePost(@PathVariable Long postId, @RequestBody PostRequestDto requestDto) {
        postService.updatePost(postId,requestDto);
    }

    //포스트 삭제
    @DeleteMapping("/api/post/{postId}")
    public void deletePost(@PathVariable Long postId) {
        postService.deletePost(postId);
    }

    //상세페이지의 좋아요(찜)
    @PostMapping("/api/love/{postId}")
    public void lovePost() {

    }
}
