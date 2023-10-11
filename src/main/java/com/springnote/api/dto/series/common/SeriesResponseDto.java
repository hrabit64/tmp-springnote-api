package com.springnote.api.dto.series.common;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.springnote.api.domain.series.Series;
import lombok.*;

@EqualsAndHashCode
@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class SeriesResponseDto {
    private Long id;
    private String title;
    private String description;

    public SeriesResponseDto(Series series){
        id = series.getId();
        title = series.getTitle();
        description = series.getDescription();
    }
}
