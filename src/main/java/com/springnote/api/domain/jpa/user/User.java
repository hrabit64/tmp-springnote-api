package com.springnote.api.domain.jpa.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    @Column(name = "user_pk", nullable = false, length = 28)
    private String id;

    @Column(name="user_is_admin", nullable = false)
    private Boolean isAdmin;

    @Column(name="user_nm", nullable = false, length = 10)
    private String name;
}
