package com.springnote.api.dto.post.service;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.springnote.api.domain.jpa.post.Post;
import com.springnote.api.domain.jpa.series.Series;
import com.springnote.api.service.PostService;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

@EqualsAndHashCode
@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostUpdateRequestServiceDto {

    private Long id;
    private String title;
    private String content;

    private String thumbnail;
    private Long seriesId;

    public Post toEntity(Series series, LocalDateTime now){
        return Post.builder()
                .title(title)
                .thumbnail(thumbnail)
                .series(series)
                .updateAt(now)
                .build();
    }

}
