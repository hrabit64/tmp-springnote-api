package com.springnote.api.domain.jpa.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;

@EqualsAndHashCode
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "user_info")
public class User {
    @Id
    @Column(name = "user_pk", nullable = false, length = 28)
    private String id;

    @Column(name="user_is_admin", nullable = false)
    private Boolean isAdmin;

    @Column(name="user_nm", nullable = false, length = 10)
    private String name;

    public void update(User newUser){
        this.name = newUser.getName();
    }
}
