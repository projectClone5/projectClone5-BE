package com.sparta.spring_projectclone.dto.requestDto;

import com.sparta.spring_projectclone.model.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@AllArgsConstructor
public class PostRequestDto {
    private String title;
    private String content;
    private Category category;
    private int price;
    private MultipartFile imgUrl;
}
