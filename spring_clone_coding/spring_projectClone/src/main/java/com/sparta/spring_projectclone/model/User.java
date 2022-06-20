package com.sparta.spring_projectclone.model;


import com.sparta.spring_projectclone.dto.requestDto.UserRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@Getter
@NoArgsConstructor
@SequenceGenerator(
        name = "USER_A",
        sequenceName = "USER_B",
        initialValue = 1, allocationSize = 50)
@Table(name = "Users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USER_A")
    @Column(name = "USER_ID")
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String password;

    @Column
    private String userImgUrl;

    @OneToMany
    private List<Post> posts = new ArrayList<>();

    @OneToMany
    private List<Love> loves = new ArrayList<>();

    public User(String username, String nickname) {
        this.username = username;
        this.nickname = nickname;
        this.password = password;
        this.userImgUrl = userImgUrl;
    }

    public User(String username, String nickname, String password) {
        this.username = username;
        this.nickname = nickname;
        this.password = password;
    }

    public User(UserRequestDto userRequestDto) {
        this.nickname = userRequestDto.getNickname();
        this.userImgUrl = userRequestDto.getUserImgUrl();
    }

    public void update(UserRequestDto userRequestDto) {
        this.nickname = userRequestDto.getNickname();
        this.userImgUrl = userRequestDto.getUserImgUrl();
    }
}
