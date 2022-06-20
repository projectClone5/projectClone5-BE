package com.sparta.spring_projectclone.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.sparta.spring_projectclone.dto.requestDto.CommentRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@SequenceGenerator(name = "COMMENT_A",
        sequenceName = "COMMENT_B",
        initialValue = 1, allocationSize = 50)
@NoArgsConstructor
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "COMMENT_A")
    @Column(name = "COMMENT_ID")
    private Long id;

    @Column(nullable = false)
    private String comment;

    @Column(nullable = false)
    private int reviewPoint;

    private String nickname;



    @JsonBackReference // 순환 참조 방지
    @ManyToOne
    @JoinColumn(name = "POST_ID")
    private Post post;


    public Comment(CommentRequestDto commentRequestDto, Post post, String username) {
        this.comment = commentRequestDto.getComment();
        this.reviewPoint = commentRequestDto.getReviewPoint();
        this.nickname = username;
        this.post = post;
    }

    public void update(CommentRequestDto commentRequestDto) {
        this.comment = commentRequestDto.getComment();
        this.reviewPoint = commentRequestDto.getReviewPoint();
    }
//    public static Comment commentCreateDto(CommentRequestDto commentCreateDto) {
//
//        Comment comment = new Comment();
//        comment.setComment(commentCreateDto.getComment());
//        comment.setReviewPoint(commentCreateDto.getReviewPoint());
////        comment.setImage(commentCreateDto.getImage());
//        return comment;

}
