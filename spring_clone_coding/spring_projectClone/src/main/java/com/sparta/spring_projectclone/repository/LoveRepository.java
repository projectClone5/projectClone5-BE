package com.sparta.spring_projectclone.repository;

import com.sparta.spring_projectclone.model.Love;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoveRepository extends JpaRepository<Love, Long> {

    Love findByPostIdAndUserId(Long postId, Long userId);
}
