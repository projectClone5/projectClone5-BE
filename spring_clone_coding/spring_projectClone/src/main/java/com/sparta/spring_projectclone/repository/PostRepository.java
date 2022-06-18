package com.sparta.spring_projectclone.repository;

import com.sparta.spring_projectclone.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {


}
