package com.sparta.spring_projectclone.service;

import com.sparta.spring_projectclone.dto.requestDto.PostRequestDto;
import com.sparta.spring_projectclone.dto.responseDto.PostCommentResponseDto;
import com.sparta.spring_projectclone.dto.responseDto.PostResponseDto;
import com.sparta.spring_projectclone.model.Comment;
import com.sparta.spring_projectclone.model.Post;
import com.sparta.spring_projectclone.repository.CommentRepository;
import com.sparta.spring_projectclone.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    private final AwsS3Service awsS3Service;

    //전체 포스트
    public List<PostResponseDto> getPost() {
        List<Post> posts = postRepository.findAll();
        List<PostResponseDto> postList = new ArrayList<>();
        for (Post post : posts) {
            PostResponseDto postResponseDto = PostResponseDto.builder()
                    .postId(post.getId())
                    .title(post.getTitle())
                    .imgUrl(post.getImgUrl())
                    .content(post.getContent())
                    .avgReviewPoint(post.getAvgReviewPoint())
                    .totalComment(post.getTotalComment())
                    .category(post.getCategory())
                    .loveCount(post.getLoveCount())
                    .price(post.getPrice())
                    .build();
            postList.add(postResponseDto);
        }
        return postList;
    }

    //포스트 상세 페이지
    public PostResponseDto getPostDetail(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new IllegalArgumentException("게시글이 존재하지 않습니다.")
        );

        List<Comment> comments = commentRepository.findAllByPostId(postId);
        List<PostCommentResponseDto> commentList = new ArrayList<>();
        for (Comment comment : comments) {
            PostCommentResponseDto postCommentResponseDto = PostCommentResponseDto.builder()
                    .commentId(comment.getId())
                    .userId(comment.getPost().getUser().getId())
                    .userImgUrl(comment.getPost().getUser().getUserImgUrl())
                    .comment(comment.getComment())
                    .reviewPoint(comment.getReviewPoint())
                    .build();
            commentList.add(postCommentResponseDto);
        }

        return PostResponseDto.builder()
                .postId(post.getId())
                .title(post.getTitle())
                .imgUrl(post.getImgUrl())
                .content(post.getContent())
                .avgReviewPoint(post.getAvgReviewPoint())
                .totalComment(post.getTotalComment())
                .category(post.getCategory())
                .loveCount(post.getLoveCount())
                .price(post.getPrice())
                .comments(commentList)
                .build();
    }

    //게시글 작성
    public void savePost(PostRequestDto requestDto, MultipartFile multipartFile) {
        Map<String, String> imgResult = awsS3Service.uploadFile(multipartFile);


        Post post = Post.builder()
                .title(requestDto.getTitle())
                .imgUrl(imgResult.get("url"))
                .content(requestDto.getContent())
                .category(requestDto.getCategory())
                .price(requestDto.getPrice())
                .build();
        postRepository.save(post);
    }

    //게시글 수정
    public void updatePost(Long postId, PostRequestDto requestDto, MultipartFile multipartFile) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new IllegalArgumentException("게시글이 존재하지 않습니다.")
        );

        if (multipartFile != null) {
            //기존 이미지 삭제후 재등록
            awsS3Service.deleteFile(post.getTransImgFileName());
            Map<String, String> imgResult = awsS3Service.uploadFile(multipartFile);

            //엔티티 업데이트

        } else {
            //엔티티 업데이트

        }

        Long userId = post.getUser().getId();


        post.update(requestDto);
        postRepository.save(post);
    }

    public void deletePost(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new IllegalArgumentException("게시글이 존재하지 않습니다.")
        );
        postRepository.deleteById(postId);
    }
}
