package com.springnote.api.dto.post.controller;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.springnote.api.dto.post.service.PostAddRequestServiceDto;
import com.springnote.api.dto.post.service.PostUploadRequestServiceDto;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@EqualsAndHashCode
@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class PostUploadRequestControllerDto {

    @NotEmpty(message = "제목을 입력해주세요.")
    @Length(max = 100, message = "제목은 50자 이내로 입력해주세요.")
    private String title;

    @NotEmpty(message = "썸네일을 입력해주세요.")
    private String thumbnail;

    @NotNull(message = "시리즈를 선택해주세요.")
    private String seriesName;

    public PostUploadRequestServiceDto toServiceDto(String content){
        return PostUploadRequestServiceDto.builder()
                .title(title)
                .content(content)
                .thumbnail(thumbnail)
                .seriesName(seriesName)
                .build();
    }
}
