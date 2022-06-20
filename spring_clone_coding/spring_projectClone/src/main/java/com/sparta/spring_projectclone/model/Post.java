package com.sparta.spring_projectclone.model;

import com.sparta.spring_projectclone.dto.requestDto.PostRequestDto;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
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

    @Lob
    @Column(nullable = false, columnDefinition = "CLOB")
    private String content;

    @Column(nullable = false)
    private int avgReviewPoint;

    @Column(nullable = false)
    private int totalReviewPoint;

    @Column(nullable = false)
    private int totalComment;

    @Column(nullable = false)
    private Category category;

    @Column(nullable = false)
    private int loveCount;

    @Column(nullable = false)
    private int price;

    @ManyToOne
    private User user;

    @OneToMany(mappedBy = "post",cascade = CascadeType.REMOVE)
    private List<Comment> comments = new ArrayList<>();

    public void modifyAvgReviewPoint(int totalReviewPoint,int totalComment) {
        this.totalReviewPoint += totalReviewPoint;
        this.totalComment += totalComment;
        this.avgReviewPoint = (int)Math.round((double)this.totalReviewPoint / this.totalComment);
    }

    public void update(PostRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
        this.category = requestDto.getCategory();
        this.price = requestDto.getPrice();
    }
}
