package com.springnote.api.service;

import com.springnote.api.domain.jpa.post.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class PostService {

    private final PostRepository postRepository;

}
