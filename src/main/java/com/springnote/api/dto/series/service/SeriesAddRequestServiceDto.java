package com.springnote.api.dto.series.service;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.springnote.api.domain.elasticsearch.seriesIndex.SeriesIndex;
import com.springnote.api.domain.jpa.series.Series;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@EqualsAndHashCode
@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SeriesAddRequestServiceDto {


    private String title;
    private String description;

    public Series toEntity(){
        return Series.builder()
                .title(title)
                .description(description)
                .build();
    }

}
