package com.sparta.spring_projectclone.service;

import com.sparta.spring_projectclone.dto.CommentRequestDto;
import com.sparta.spring_projectclone.dto.CommentResponseDto;
import com.sparta.spring_projectclone.model.Comment;
import com.sparta.spring_projectclone.repository.CommentRepository;
import com.sparta.spring_projectclone.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CommentService {


    private final PostRepository postRepository;
    private final CommentRepository commentRepository;


    // 댓글 작성
    @Transactional
    public CommentResponseDto commentWrite( CommentRequestDto commentRequestDto) {

//        Post post = postRepository.findById(postId).orElseThrow(
//                () -> new IllegalArgumentException("게시글이 존재하지 않습니다."));
        Comment comment = Comment.commentCreateDto(commentRequestDto);
        commentRepository.save(comment);
        CommentResponseDto commentResponseDto = new CommentResponseDto(comment);
        return commentResponseDto;


    }
}
