package com.sparta.spring_projectclone.controller;

import com.sparta.spring_projectclone.dto.CommentRequestDto;
import com.sparta.spring_projectclone.exception.ApiResponseMessage;
import com.sparta.spring_projectclone.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


@Validated
@RestController
@RequiredArgsConstructor
public class commentController {

    private final CommentService commentService;

//    @PostMapping("api/post/{postId}/comment")
//    public ResponseEntity<ApiResponseMessage> commentWrite(@PathVariable("postId") Long postId, @RequestBody @Valid CommentRequestDto commentRequestDto){
//        commentService.commentWrite(postId, commentRequestDto);
//        ApiResponseMessage message = new ApiResponseMessage("Success", "댓글이 작성 되었습니다.", "", "");
//        return new  ResponseEntity<ApiResponseMessage>(message, HttpStatus.OK);
@PostMapping("/api/post/comment")
public ResponseEntity<ApiResponseMessage> commentWrite(@RequestBody @Valid CommentRequestDto commentRequestDto){
    commentService.commentWrite(commentRequestDto);
    ApiResponseMessage message = new ApiResponseMessage("Success", "댓글이 작성 되었습니다.", "", "");
    return new  ResponseEntity<ApiResponseMessage>(message, HttpStatus.OK);
        /*
                public ResponseEntity<ApiResponseMessage> commentWrite(@PathVariable("id") Long postId, @AuthenticationPrincipal UserDetailsImpl userDetails){
                return commentService.commentWrite(commentCreateDto, userDetails.getUsername());
                ApiResponseMessage message = new ApiResponseMessage("Success", "게시글이 작성 되었습니다.", "", "");
                return new  ResponseEntity<ApiResponseMessage>(message, HttpStatus.OK);
         * */
    }
}
