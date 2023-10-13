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

    @Column(name = "image_origin_nm", nullable = false)
    private String originName;

    @Column(name = "image_save_nm", nullable = false)
    private String saveName;

    @Column(name = "image_origin_type", nullable = false)
    private String originType;

    @Column(name = "image_create_at", nullable = false)
    private LocalDateTime createAt;
}
