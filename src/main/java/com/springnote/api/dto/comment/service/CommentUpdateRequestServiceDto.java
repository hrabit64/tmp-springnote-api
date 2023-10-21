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
public class CommentUpdateRequestServiceDto {
    private Long id;
    private String content;
    private String writer;

    public Comment toEntity(LocalDateTime now){
        return Comment.builder()
                .content(content)
                .updateAt(now)
                .build();
    }
}
