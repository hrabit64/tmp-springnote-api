package com.springnote.api.dto.comment.controller;

import com.springnote.api.dto.comment.service.CommentUpdateRequestServiceDto;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@EqualsAndHashCode
@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentUpdateRequestControllerDto {

    @NotEmpty(message = "내용을 입력해주세요.")
    @Length(min = 1, max = 500,message = "내용은 1자 이상 500자 이하로 입력해주세요.")
    private String content;

    public CommentUpdateRequestServiceDto toServiceDto(String writer,Long id){
        return CommentUpdateRequestServiceDto.builder()
                .id(id)
                .writer(writer)
                .content(content)
                .build();
    }
}
