package com.sparta.spring_projectclone.dto.requestDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDto {
    private String nickname;
    private String userImgUrl;

    public UserRequestDto(String nickname) {
        this.nickname = nickname;
    }
}
