package com.sparta.spring_projectclone.service;

import com.sparta.spring_projectclone.dto.requestDto.PostRequestDto;
import com.sparta.spring_projectclone.dto.responseDto.LoveResponseDto;
import com.sparta.spring_projectclone.dto.responseDto.PostCommentResponseDto;
import com.sparta.spring_projectclone.dto.responseDto.PostResponseDto;
import com.sparta.spring_projectclone.model.Comment;
import com.sparta.spring_projectclone.model.Love;
import com.sparta.spring_projectclone.model.Post;
import com.sparta.spring_projectclone.model.User;
import com.sparta.spring_projectclone.repository.CommentRepository;
import com.sparta.spring_projectclone.repository.LoveRepository;
import com.sparta.spring_projectclone.repository.PostRepository;
import com.sparta.spring_projectclone.repository.UserRepository;
import com.sparta.spring_projectclone.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    private final LoveRepository loveRepository;

    private final AwsS3Service awsS3Service;

    //전체 포스트
    public List<PostResponseDto> getPost() {
        List<Post> posts = postRepository.findAll();
        List<PostResponseDto> postList = new ArrayList<>();
        for (Post post : posts) {
            List<Love> postLoves = loveRepository.findAllByPostId(post.getId());
            List<LoveResponseDto> loveUserIdList = new ArrayList<>();
            for (Love love : postLoves) {
                Long userId = love.getUserId();
                LoveResponseDto loveResponseDto = new LoveResponseDto(userId);
                loveUserIdList.add(loveResponseDto);
            }
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
                    .loves(loveUserIdList)
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
        List<Love> loves = post.getLoves();
        List<LoveResponseDto> loveUserIdList = new ArrayList<>();
        for (Love love : loves) {
            Long userId = love.getUserId();
            LoveResponseDto loveResponseDto = new LoveResponseDto(userId);
            loveUserIdList.add(loveResponseDto);
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
                .loves(loveUserIdList)
                .build();
    }

    //게시글 작성
    public void savePost(PostRequestDto postRequestDto, UserDetailsImpl userDetails) {
        User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow(
                () -> new IllegalArgumentException("유저가 존재하지 않습니다.")
        );

        Map<String, String> imgResult = awsS3Service.uploadFile(postRequestDto.getImgUrl());

        Post post = Post.builder()
                .title(postRequestDto.getTitle())
                .imgUrl(imgResult.get("url"))
                .transImgFileName(imgResult.get("transImgFileName"))
                .content(postRequestDto.getContent())
                .category(postRequestDto.getCategory())
                .price(postRequestDto.getPrice())
                .user(user)
                .build();
        postRepository.save(post);
    }

    //게시글 수정
    public void updatePost(Long postId, PostRequestDto requestDto, UserDetailsImpl userDetails) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new IllegalArgumentException("게시글이 존재하지 않습니다.")
        );
        //게시글 작성자가 현재 로그인한 사람인지 확인
        validateUser(userDetails, post);

        if (requestDto.getImgUrl() != null) {
            //기존 이미지 삭제후 재등록
            awsS3Service.deleteFile(post.getTransImgFileName());
            Map<String, String> imgResult = awsS3Service.uploadFile(requestDto.getImgUrl());
            //엔티티 업데이트
            post.update(requestDto,imgResult);
        } else {
            post.update(requestDto);
        }
        postRepository.save(post);
    }

    public void deletePost(Long postId, UserDetailsImpl userDetails) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new IllegalArgumentException("게시글이 존재하지 않습니다.")
        );
        //게시글 작성자가 현재 로그인한 사람인지 확인
        validateUser(userDetails,post);

        //이미지 파일 삭제
        awsS3Service.deleteFile(post.getTransImgFileName());
        postRepository.delete(post);
    }

    private void validateUser(UserDetailsImpl userDetails, Post post) {
        if (!post.getUser().getUsername().equals(userDetails.getUsername())) {
            throw new IllegalArgumentException("게시글 작성자만 수정할 수 있습니다.");
        }
    }
}
