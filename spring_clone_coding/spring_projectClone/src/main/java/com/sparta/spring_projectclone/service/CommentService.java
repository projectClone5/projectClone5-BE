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
    public CommentResponseDto commentWrite(Long postId, CommentRequestDto commentRequestDto , String username) {

        Post post = postRepository.findById(postId).orElseThrow(
                () -> new IllegalArgumentException("게시글이 존재하지 않습니다."));
        Comment comment = new Comment(commentRequestDto , post, username);
        System.out.println("옴????");
        CommentResponseDto commentResponseDto = new CommentResponseDto(comment);
            post.modifyAvgReviewPoint(commentRequestDto.getReviewPoint(), 1);
        commentRepository.save(comment);
        return commentResponseDto;
    }

    //댓글 수정
    @Transactional
    public CommentResponseDto commentUpdated(Long commentId, CommentRequestDto commentRequestDto, String username){
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new IllegalArgumentException("댓글이 존재하지 않습니다."));
        String writer = comment.getUsername();
        System.out.println("옴?");
        // 본인이 작성한 글이 아닙니다
        if(writer.equals(username)){
            //Post에 리뷰포인트 수정
            comment.getPost().modifyAvgReviewPoint(commentRequestDto.getReviewPoint() - comment.getReviewPoint(), 0);
            comment.update(commentRequestDto);
            CommentResponseDto CommentResponseDto = new CommentResponseDto(comment);
            return CommentResponseDto;
        } else {
            throw new IllegalArgumentException("작성한 유저가 아닙니다.");
        }
    }

    // 댓글 삭제
    public void commentDelete(Long commentId, String username) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new IllegalArgumentException("댓글이 존재하지 않습니다."));
        String writer = comment.getUsername();
        // 본인이 작성한 글이 아닙니다
        if(writer.equals(username)){
            //Post에 리뷰포인트 삭제
            comment.getPost().modifyAvgReviewPoint(-comment.getReviewPoint(), -1);
            commentRepository.delete(comment);
        } else {
            throw new IllegalArgumentException("작성한 유저가 아닙니다.");
        }
    }
}
