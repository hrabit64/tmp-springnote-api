package com.springnote.api.dto.comment.common;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.springnote.api.domain.jpa.comment.Comment;
import com.springnote.api.dto.user.common.UserResponseDto;
import lombok.*;

@EqualsAndHashCode
@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class CommentResponseDto {
    private Long id;
    private String content;
    private UserResponseDto writer;
    private Long replyCount;
    private Boolean isReply;

    public CommentResponseDto(Comment comment){
        if(comment.getIsDeleted()){
            id = comment.getId();
            content = "삭제된 댓글입니다.";
            writer = new UserResponseDto(comment.getUser());
            replyCount = (comment.getReplyCnt() != null) ? comment.getReplyCnt() : 0;
            isReply = comment.getIsReply();
        }else{
            id = comment.getId();
            content = comment.getContent();
            writer = new UserResponseDto(comment.getUser());
            replyCount = (comment.getReplyCnt() != null) ? comment.getReplyCnt() : 0;
            isReply = comment.getIsReply();
        }

    }
}
