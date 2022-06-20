package com.sparta.spring_projectclone.controller;

import com.sparta.spring_projectclone.dto.requestDto.CommentRequestDto;
import com.sparta.spring_projectclone.dto.responseDto.CommentResponseDto;
import com.sparta.spring_projectclone.exception.ApiResponseMessage;
import com.sparta.spring_projectclone.security.UserDetailsImpl;
import com.sparta.spring_projectclone.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@Validated
@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    //댓글 작성
    @PostMapping("/api/post/{postId}/comment")
    public ResponseEntity<ApiResponseMessage> commentWrite(@PathVariable("postId") Long postId, @RequestBody @Valid CommentRequestDto commentRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        commentService.commentWrite(postId, commentRequestDto, userDetails.getUsername());
        ApiResponseMessage message = new ApiResponseMessage("Success", "댓글이 작성 되었습니다.", "", "");
        return new  ResponseEntity<ApiResponseMessage>(message, HttpStatus.OK);
    }
    //댓글 수정
    @PutMapping("/api/comment/{commentId}")
    public CommentResponseDto commentUpdate(@PathVariable("commentId") Long commentId, @RequestBody @Valid CommentRequestDto commentRequestDto , @AuthenticationPrincipal UserDetailsImpl userDetails){
        return commentService.commentUpdated(commentId , commentRequestDto );
    }
    //댓글 삭제
    @DeleteMapping("/api/comment/{commentId}")
    public ResponseEntity<ApiResponseMessage> commentDelete(@PathVariable("commentId") Long commentId , @AuthenticationPrincipal UserDetailsImpl userDetails) {
        commentService.commentDelete(commentId);
        ApiResponseMessage message = new ApiResponseMessage("Success", "댓글이 삭제 되었습니다.", "", "");
        return new ResponseEntity<ApiResponseMessage>(message, HttpStatus.OK);
    }
}
