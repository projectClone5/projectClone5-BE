package com.sparta.spring_projectclone.dto.responseDto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostCommentResponseDto {

    private Long commentId;
    private Long userId;
    private String userImgUrl;
    private String comment;
    private int reviewPoint;
}
