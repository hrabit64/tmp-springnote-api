package com.springnote.api.dto.series.service;

import com.springnote.api.domain.jpa.series.Series;
import lombok.*;

@EqualsAndHashCode
@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SeriesUpdateRequestServiceDto {

    private Long id;
    private String title;
    private String description;

    public Series toEntity(){
        return Series.builder()
                .title(title)
                .description(description)
                .build();
    }

}
