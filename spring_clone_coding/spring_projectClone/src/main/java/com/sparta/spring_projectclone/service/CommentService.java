package com.sparta.spring_projectclone.service;

import com.sparta.spring_projectclone.dto.requestDto.CommentRequestDto;
import com.sparta.spring_projectclone.dto.responseDto.CommentResponseDto;
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
    public CommentResponseDto commentWrite(Long postId, CommentRequestDto commentRequestDto) {

        Post post = postRepository.findById(postId).orElseThrow(
                () -> new IllegalArgumentException("게시글이 존재하지 않습니다."));
        Comment comment = new Comment(commentRequestDto, post);
        CommentResponseDto commentResponseDto = new CommentResponseDto(comment);
        //Post에 리뷰포인트, 코멘트 수 추가
        post.modifyAvgReviewPoint(commentRequestDto.getReviewPoint(), 1);
        commentRepository.save(comment);

        return commentResponseDto;


//        Comment comment = Comment.commentCreateDto(commentRequestDto);
//        commentRepository.save(comment , post);
//        CommentResponseDto commentResponseDto = new CommentResponseDto(comment);
//        return commentResponseDto;
    }

    //댓글 수정
    @Transactional
    public CommentResponseDto commentUpdated(Long commentId, CommentRequestDto commentRequestDto) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new IllegalArgumentException("댓글이 존재하지 않습니다."));

        //Post에 리뷰포인트 수정
        comment.getPost().modifyAvgReviewPoint(commentRequestDto.getReviewPoint() - comment.getReviewPoint(), 0);

        comment.update(commentRequestDto);
        commentRepository.save(comment);
        return new CommentResponseDto(comment);
    }

    // 댓글 삭제
    public void commentDelete(Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new IllegalArgumentException("댓글이 존재하지 않습니다."));
        //Post에 리뷰포인트 삭제
        comment.getPost().modifyAvgReviewPoint(-comment.getReviewPoint(), -1);
        commentRepository.delete(comment);
    }
}
