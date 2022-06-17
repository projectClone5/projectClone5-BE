package com.sparta.spring_projectclone.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@SequenceGenerator(name = "POST_A",
        sequenceName = "POST_B",
        initialValue = 1, allocationSize = 50)
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "POST_A")
    @Column(name = "POST_ID")
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String imgUrl;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private double avgReviewPoint;

    @Column(nullable = false)
    private int totalComment;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private int loveCount;

    @Column(nullable = false)
    private int price;

//    @ManyToOne
//    private User user;
//
//    @OneToMany
//    private List<Comment> comments = new ArrayList<>();
}
