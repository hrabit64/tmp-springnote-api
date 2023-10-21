package com.springnote.api.dto.comment.service;

import com.springnote.api.domain.jpa.comment.Comment;
import com.springnote.api.domain.jpa.post.Post;
import com.springnote.api.domain.jpa.user.User;
import lombok.*;

import java.time.LocalDateTime;

@EqualsAndHashCode
@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentAddRequestServiceDto {
    private String content;
    private Long postId;
    private String writer;

    public Comment toEntity(User user, Post post, LocalDateTime now){
        return Comment.builder()
                .parentComment(null)
                .isReply(false)
                .user(user)
                .post(post)
                .replyCnt(0L)
                .content(content)
                .createAt(now)
                .updateAt(now)
                .build();
    }
}
