package com.springnote.api.service;

import com.springnote.api.domain.jpa.post.PostRepository;
import com.springnote.api.dto.post.common.PostResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class PostService {

    private final PostRepository postRepository;

    public PostResponseDto getPostById(Long postId) {
        return postRepository.findById(postId).map(PostResponseDto::new).orElseThrow();
    }

}
