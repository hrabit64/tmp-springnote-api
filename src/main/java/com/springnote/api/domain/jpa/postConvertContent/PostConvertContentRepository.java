package com.springnote.api.domain.jpa.postConvertContent;

import com.springnote.api.domain.jpa.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostConvertContentRepository extends JpaRepository<PostConvertContent, Long> {
    Optional<PostConvertContent> findByPost(Post post);
}