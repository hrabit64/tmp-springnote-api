package com.springnote.api.domain.jpa.postContent;

import com.springnote.api.domain.jpa.post.Post;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostContentRepository extends JpaRepository<PostContent, Long> {

    @EntityGraph(value = "PostContent.post")
    Optional<PostContent> findByPost(Post post);

}