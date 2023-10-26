package com.springnote.api.domain.jpa.post;

import com.springnote.api.domain.elasticsearch.postIndex.PostIndex;
import com.springnote.api.domain.jpa.comment.Comment;
import com.springnote.api.domain.jpa.postConvertContent.PostConvertContent;
import com.springnote.api.domain.jpa.series.Series;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@NamedEntityGraphs({
        @NamedEntityGraph(name = "Post.series", attributeNodes = @NamedAttributeNode("series"))
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "post")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_pk", nullable = false)
    private Long id;

    @Column(name = "post_title", nullable = false, length = 50)
    private String title;


    @Column(name = "post_create_at", nullable = false)
    private LocalDateTime createAt;

    @Column(name = "post_update_at", nullable = false)
    private LocalDateTime updateAt;

    @Column(name = "post_thumbnail", nullable = false)
    private String thumbnail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "series_series_pk", nullable = false)
    private Series series;


    public PostIndex toIndex(String content){
        return PostIndex.builder()
                .id(id)
                .title(title)
                .content(content)
                .build();
    }

    public void update(Post post){
        title = post.getTitle();
        thumbnail = post.getThumbnail();
        updateAt = post.getUpdateAt();
    }
}
