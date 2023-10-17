package com.springnote.api.dto.series.controller;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.springnote.api.dto.series.service.SeriesAddRequestServiceDto;
import com.springnote.api.dto.series.service.SeriesUpdateRequestServiceDto;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@EqualsAndHashCode
@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class SeriesUpdateRequestControllerDto {

    @NotEmpty(message = "제목은 필수값입니다.")
    @Length(min = 2, max = 50, message = "제목은 2자 이상 50자 이하입니다.")
    private String title;

    @Length(max = 50, message = "설명은 최대 50자까지 입력가능합니다.")
    private String description;

    public SeriesUpdateRequestServiceDto toServiceDto(Long id){
        return SeriesUpdateRequestServiceDto.builder()
                .id(id)
                .title(title)
                .description(description)
                .build();
    }
}
