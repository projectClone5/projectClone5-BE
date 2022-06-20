package com.sparta.spring_projectclone.dto.responseDto;

import java.util.List;

public class UserResponseDto {

    private Long userId;
    private String nickname;
    private String userImgUrl;
    private List<PostResponseDto> post;
}
