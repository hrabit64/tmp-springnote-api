package com.springnote.api.domain.elasticsearch.seriesIndex;

import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.elasticsearch.annotations.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Mapping(mappingPath = "elastic/series-mapping.json")
@Setting(settingPath = "elastic/series-setting.json")
@Document(indexName = "api-series_index")
public class SeriesIndex {

    @Id
    private Long id;
    private String title;
    private String description;

    public void update(SeriesIndex seriesIndex){
        this.title = seriesIndex.getTitle();
        this.description = seriesIndex.getDescription();
    }

}
