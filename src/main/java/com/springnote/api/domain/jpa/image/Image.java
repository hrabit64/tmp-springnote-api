package com.springnote.api.domain.jpa.image;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "image")
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_pk", nullable = false)
    private Long id;

    @Column(name = "image_name", nullable = false)
    private String name;

    @Column(name = "image_origin_name", nullable = false)
    private String originName;

    @Column(name = "image_create_at", nullable = false)
    private LocalDateTime createAt;
}
