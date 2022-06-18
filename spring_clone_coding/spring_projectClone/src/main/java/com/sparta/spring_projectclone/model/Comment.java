package com.sparta.spring_projectclone.model;


import com.sparta.spring_projectclone.dto.requestDto.CommentRequestDto;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@SequenceGenerator(name = "COMMENT_A",
        sequenceName = "COMMENT_B",
        initialValue = 1, allocationSize = 50)
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "COMMENT_A")
    @Column(name = "COMMENT_ID")
    private Long id;

    @Column
    private String comment;

    @Column(nullable = false)
    private int reviewPoint;


    @ManyToOne
    private Post post;

    @Column
    private String image;


//    @ManyToOne
//    private Post post;


    public static Comment commentCreateDto(CommentRequestDto commentCreateDto) {

        Comment comment = new Comment();
        comment.setComment(commentCreateDto.getComment());
        comment.setReviewPoint(commentCreateDto.getReviewPoint());
        comment.setImage(commentCreateDto.getImage());

        return comment;
    }
}
