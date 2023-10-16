package com.springnote.api.dto.user.service;

import com.springnote.api.domain.jpa.user.User;
import lombok.*;

@EqualsAndHashCode
@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserUpdateRequestServiceDto {
    private String uid;
    private String name;

    public User toEntity() {
        return User.builder()
                .name(name)
                .build();
    }

}
