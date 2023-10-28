package com.springnote.api.domain.elasticsearch.postIndex;

import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.elasticsearch.annotations.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Mapping(mappingPath = "elastic/post-mapping.json")
@Setting(settingPath = "elastic/post-setting.json")
@Document(indexName = "springnote-post_index")
public class PostIndex {

    @Id
    private Long id;
    private String title;
    private String content;

}
