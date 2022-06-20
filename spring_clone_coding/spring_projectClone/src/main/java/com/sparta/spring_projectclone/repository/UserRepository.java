package com.sparta.spring_projectclone.repository;

import com.sparta.spring_projectclone.dto.responseDto.UserResponseDto;
import com.sparta.spring_projectclone.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    UserResponseDto findAllById(Long userId);

    Optional<User> findByNickname(String nickname);
}
