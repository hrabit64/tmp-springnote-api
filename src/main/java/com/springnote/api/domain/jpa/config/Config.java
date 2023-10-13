package com.springnote.api.domain.jpa.config;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "config")
public class Config {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "config_pk", nullable = false, length = 20)
    private String id;

    @Column(name="config_val", nullable = false, length = 200)
    private String key;
}
