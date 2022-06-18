package com.sparta.spring_projectclone.repository;

import com.sparta.spring_projectclone.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

}
