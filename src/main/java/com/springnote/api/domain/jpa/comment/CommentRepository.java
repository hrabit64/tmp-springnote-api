package com.springnote.api.domain.jpa.comment;

import com.springnote.api.domain.jpa.post.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @EntityGraph(attributePaths = {"user", "post"})
    Page<Comment> findAllByIsReplyAndPost(Pageable pageable, Boolean isReply, Post post);

    @EntityGraph(attributePaths = {"user", "post","parentComment"})
    Page<Comment> findAllByParentComment(Pageable pageable, Comment parentComment);
}