package com.springnote.api.domain.jpa.postConvertContent;

import com.springnote.api.domain.jpa.post.Post;
import jakarta.persistence.*;
import lombok.*;


//html 변환된 본문
@NamedEntityGraphs({
        @NamedEntityGraph(name = "PostConvertContent.post", attributeNodes = @NamedAttributeNode("post"))
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "post_convert_content")
public class PostConvertContent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_convert_content_pk", nullable = false)
    private Long id;

    @Column(name = "post_convert_content_text", nullable = false)
    private String text;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_post_pk", nullable = false)
    private Post post;

    public void update(String text){
        this.text = text;
    }
}
