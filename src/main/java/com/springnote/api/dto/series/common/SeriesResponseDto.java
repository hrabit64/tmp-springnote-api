package com.springnote.api.dto.series.common;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.springnote.api.domain.elasticsearch.seriesIndex.SeriesIndex;
import com.springnote.api.domain.jpa.series.Series;
import lombok.*;
import org.springframework.hateoas.server.core.Relation;

@EqualsAndHashCode
@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Relation(collectionRelation = "series_items")
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

    public SeriesResponseDto(SeriesIndex seriesIndex){
        id = seriesIndex.getId();
        title = seriesIndex.getTitle();
        description = seriesIndex.getDescription();
    }
}
