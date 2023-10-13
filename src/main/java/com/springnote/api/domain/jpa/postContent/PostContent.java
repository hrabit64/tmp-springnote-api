package com.springnote.api.domain.jpa.postContent;

import com.springnote.api.domain.jpa.post.Post;
import jakarta.persistence.*;
import lombok.*;

@NamedEntityGraphs({
        @NamedEntityGraph(name = "PostContent.post", attributeNodes = @NamedAttributeNode("post"))
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "post_content")
public class PostContent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_content_pk", nullable = false)
    private Long id;

    @Column(name = "post_content_text", nullable = false)
    private String text;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_post_pk", nullable = false)
    private Post post;
}
