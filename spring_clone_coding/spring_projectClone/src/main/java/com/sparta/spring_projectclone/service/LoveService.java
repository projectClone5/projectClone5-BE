package com.sparta.spring_projectclone.service;

import com.sparta.spring_projectclone.model.Love;
import com.sparta.spring_projectclone.model.Post;
import com.sparta.spring_projectclone.repository.LoveRepository;
import com.sparta.spring_projectclone.repository.PostRepository;
import com.sparta.spring_projectclone.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LoveService {

    private final LoveRepository loveRepository;
    private final PostRepository postRepository;
    public void loves(Long postId, UserDetailsImpl userDetails) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new IllegalArgumentException("포스트가 없습니다")
        );
        Love love = new Love(userDetails.getUser(), post);
        loveRepository.save(love);
    }

    public void unloved(Long postId, UserDetailsImpl userDetails) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new IllegalArgumentException("포스트가 없습니다.")
        );
        Love love = loveRepository.findByPostIdAndUserId(postId, userDetails.getUser().getId());
        if (love == null) {
            throw new IllegalArgumentException("찜을 한 내역이 없습니다.");
        }else {
            loveRepository.delete(love);
        }

    }
}
