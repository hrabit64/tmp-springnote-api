package com.springnote.api.domain.elasticsearch.postIndex;

import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(indexName = "springnote-post_index")
public class PostIndex {

    @Id
    @Field(type= FieldType.Long)
    private Long id;

    @Field(type= FieldType.Text)
    private String title;

    @Field(type= FieldType.Text)
    private String content;

}
