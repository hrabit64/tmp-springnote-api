package com.springnote.api.dto.post.service;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.springnote.api.domain.jpa.post.Post;
import com.springnote.api.dto.post.controller.PostResponseControllerDto;
import com.springnote.api.dto.series.common.SeriesResponseDto;
import lombok.*;

import java.time.LocalDateTime;

@EqualsAndHashCode
@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostResponseServiceDto {
    private Long id;
    private String title;
    private LocalDateTime createdAt;
    private LocalDateTime updateAt;
    private String thumbnail;
    private SeriesResponseDto series;
    private String content;

    private boolean isRendered;

    public PostResponseServiceDto(Post post, String content, boolean isRendered){
        id = post.getId();
        title = post.getTitle();
        createdAt = post.getCreateAt();
        updateAt = post.getUpdateAt();
        thumbnail = post.getThumbnail();
        series = new SeriesResponseDto(post.getSeries());
        this.content = content;
        this.isRendered = isRendered;
    }

    public PostResponseControllerDto toControllerDto(){
        return PostResponseControllerDto.builder()
                .id(id)
                .title(title)
                .createdAt(createdAt)
                .updateAt(updateAt)
                .thumbnail(thumbnail)
                .series(series)
                .content(content)
                .build();
    }
}
