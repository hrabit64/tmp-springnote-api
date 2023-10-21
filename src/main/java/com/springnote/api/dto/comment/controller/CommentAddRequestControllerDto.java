package com.springnote.api.dto.comment.controller;

import com.springnote.api.domain.jpa.comment.Comment;
import com.springnote.api.domain.jpa.post.Post;
import com.springnote.api.domain.jpa.user.User;
import com.springnote.api.dto.comment.service.CommentAddRequestServiceDto;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

@EqualsAndHashCode
@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentAddRequestControllerDto {

    @NotEmpty(message = "내용을 입력해주세요.")
    @Length(min = 1, max = 500,message = "내용은 1자 이상 500자 이하로 입력해주세요.")
    private String content;

    @NotNull(message = "대상 게시글을 선택해주세요.")
    private Long postId;

    public CommentAddRequestServiceDto toServiceDto(String writer){
        return CommentAddRequestServiceDto.builder()
                .content(content)
                .writer(writer)
                .postId(postId)
                .build();
    }
}
