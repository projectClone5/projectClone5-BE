package com.sparta.spring_projectclone.dto.requestDto;


import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CommentRequestDto {
    @NotNull(message = "내용을 입력해 주세요.")
    private String comment;

    @NotNull(message = "내용을 입력해 주세요.")
    private int reviewPoint;

    private String image;
}

