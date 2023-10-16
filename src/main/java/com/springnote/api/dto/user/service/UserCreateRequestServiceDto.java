package com.springnote.api.dto.user.service;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.springnote.api.domain.jpa.user.User;
import lombok.*;

@EqualsAndHashCode
@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCreateRequestServiceDto {
    private String uid;
    private String name;
    private boolean isAdmin;

    public User toEntity() {
        return User.builder()
                .id(uid)
                .name(name)
                .isAdmin(isAdmin)
                .build();
    }

}
