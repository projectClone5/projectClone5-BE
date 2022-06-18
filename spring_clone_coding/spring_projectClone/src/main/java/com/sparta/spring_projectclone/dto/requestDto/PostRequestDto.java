package com.sparta.spring_projectclone.dto.requestDto;

import com.sparta.spring_projectclone.model.Category;
import lombok.Getter;

@Getter
public class PostRequestDto {
    private String title;
    private String imgUrl;
    private String content;
    private Category category;
    private int price;
}
