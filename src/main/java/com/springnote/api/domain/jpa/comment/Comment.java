package com.springnote.api.domain.jpa.comment;

import com.springnote.api.domain.jpa.post.Post;
import com.springnote.api.domain.jpa.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@NamedEntityGraphs({
        @NamedEntityGraph(name = "Comment.post", attributeNodes = @NamedAttributeNode("post")),
        @NamedEntityGraph(name = "Comment.user", attributeNodes = @NamedAttributeNode("user")),
        @NamedEntityGraph(name = "Comment.comment", attributeNodes = @NamedAttributeNode("parentComment"))
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "comment")
public class Comment {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    @Column(name = "comment_pk", nullable = false)
    private Long id;

    @Column(name = "comment_is_reply", nullable = false)
    private Boolean isReply;

    @Column(name = "comment_content", nullable = false)
    private String content;


    @Column(name="comment_reply_cnt",nullable = true)
    private Long replyCnt;

    @Column(name = "comment_create_at")
    private LocalDateTime createAt;

    @Column(name = "comment_update_at")
    private LocalDateTime updateAt;

    @Column(name = "comment_is_deleted")
    private Boolean isDeleted;

    @Column(name = "comment_delete_at")
    private LocalDateTime deleteAt;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_comment_pk")
    private Comment parentComment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_post_pk", nullable = false)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_user_pk", nullable = false)
    private User user;

    public void update(Comment comment){
        this.content = comment.getContent();
        this.updateAt = comment.getUpdateAt();
    }

    public void addReply(){
        this.replyCnt++;
    }

    public void setDelete(LocalDateTime now){
        this.isDeleted = true;
        this.deleteAt = now;
    }
}
