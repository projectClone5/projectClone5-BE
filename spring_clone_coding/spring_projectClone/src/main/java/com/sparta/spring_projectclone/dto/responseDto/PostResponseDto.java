package com.sparta.spring_projectclone.dto.responseDto;

import com.sparta.spring_projectclone.model.Category;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class PostResponseDto {

    private Long postId;
    private String title;
    private String imgUrl;
    private String content;
    private int avgReviewPoint;
    private int totalComment;
    private Category category;
    private int loveCount;
    private int price;
    private List<CommentResponseDto> comments;
}
