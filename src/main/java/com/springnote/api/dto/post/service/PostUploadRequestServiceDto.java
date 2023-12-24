package com.springnote.api.dto.post.service;

import com.springnote.api.domain.jpa.post.Post;
import com.springnote.api.domain.jpa.series.Series;
import lombok.*;

import java.time.LocalDateTime;

@EqualsAndHashCode
@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostUploadRequestServiceDto {

    private String title;

    //markdown 형식의 콘텐츠 본문
    private String content;
    
    private String thumbnail;
    private String seriesName;


}
