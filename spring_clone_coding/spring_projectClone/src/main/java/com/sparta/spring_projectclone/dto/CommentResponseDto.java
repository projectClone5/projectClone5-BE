package com.sparta.spring_projectclone.dto;


import com.sparta.spring_projectclone.model.Comment;

public class CommentResponseDto {
    private String comment;
    private int reviewPoint;
    private String image;

    public CommentResponseDto(Comment comment) {
    }
}
