package com.springnote.api.dto.post.controller;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.springnote.api.dto.post.service.PostAddRequestServiceDto;
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
public class PostAddRequestControllerDto {

    @NotEmpty(message = "제목을 입력해주세요.")
    @Length(max = 100, message = "제목은 50자 이내로 입력해주세요.")
    private String title;

    @NotEmpty(message = "내용을 입력해주세요.")
    private String content;

    @NotEmpty(message = "썸네일을 입력해주세요.")
    private String thumbnail;

    @NotNull(message = "시리즈를 선택해주세요.")
    @Positive(message = "시리즈 ID 형식이 잘못됬습니다.")
    private Long seriesId;

    public PostAddRequestServiceDto toServiceDto(){
        return PostAddRequestServiceDto.builder()
                .title(title)
                .content(content)
                .thumbnail(thumbnail)
                .seriesId(seriesId)
                .build();
    }
}
