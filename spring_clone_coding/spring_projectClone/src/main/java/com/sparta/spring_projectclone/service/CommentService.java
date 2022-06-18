package com.sparta.spring_projectclone.service;

import com.sparta.spring_projectclone.dto.CommentCreateDto;
import com.sparta.spring_projectclone.dto.CommentResponseDto;
import com.sparta.spring_projectclone.model.Comment;
import com.sparta.spring_projectclone.model.Post;
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
    public CommentResponseDto commentWrite(Long postId, CommentCreateDto commentCreateDto) {

        Post post = postRepository.findById(postId).orElseThrow(
                () -> new IllegalArgumentException("게시글이 존재하지 않습니다."));
        Comment comment = Comment.commentCreateDto(postId, commentCreateDto);

        commentRepository.save(comment);
        CommentResponseDto commentResponseDto = new CommentResponseDto(comment);
        return commentResponseDto;

//        noticeRepository.save(notice);
//        NoticeResponseDto noticeResponseDto = new NoticeResponseDto(notice);
//        return noticeResponseDto;

    }
}
