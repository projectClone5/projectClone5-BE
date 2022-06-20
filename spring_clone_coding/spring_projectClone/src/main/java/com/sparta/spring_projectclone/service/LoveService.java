package com.sparta.spring_projectclone.service;

import com.sparta.spring_projectclone.model.Love;
import com.sparta.spring_projectclone.model.Post;
import com.sparta.spring_projectclone.repository.LoveRepository;
import com.sparta.spring_projectclone.repository.PostRepository;
import com.sparta.spring_projectclone.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoveService {

    private final LoveRepository loveRepository;
    private final PostRepository postRepository;

    public void loves(Long postId, UserDetailsImpl userDetails) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new IllegalArgumentException("포스트가 없습니다")
        );

        Love foundLove = loveRepository.findByPostIdAndUserId(postId, userDetails.getUser().getId());
        if (foundLove == null) {
            Love love = new Love(userDetails.getUser().getId(), post);
            post.modifyLoveCount(1);
            loveRepository.save(love);
        } else {
            post.modifyLoveCount(-1);
            loveRepository.delete(foundLove);
        }
    }
}
