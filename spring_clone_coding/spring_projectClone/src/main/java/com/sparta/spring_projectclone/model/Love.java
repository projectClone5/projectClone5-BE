package com.sparta.spring_projectclone.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@SequenceGenerator(name = "LOVE_A",
        sequenceName = "LOVE_B",
        initialValue = 1, allocationSize = 50)
public class Love {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Love_A")
    @Column(name = "LOVE_ID")
    private Long id;

    @ManyToOne
    private Post post;

    @Column
    private Long userId;


    public Love(Long userId, Post post) {
        this.userId = userId;
        this.post = post;
    }
}
