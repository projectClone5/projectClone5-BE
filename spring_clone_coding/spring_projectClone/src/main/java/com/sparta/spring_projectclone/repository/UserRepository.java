package com.sparta.spring_projectclone.repository;

import com.sparta.spring_projectclone.dto.responseDto.UserResponseDto;
import com.sparta.spring_projectclone.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByNickname(String nickname);

    List<User> findAllById(Long userId);
}
