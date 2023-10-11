package com.springnote.api.dto.user.common;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.springnote.api.domain.user.User;
import lombok.*;

@EqualsAndHashCode
@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class UserResponseDto {
    private String uid;
    private boolean isAdmin;
    private String name;

    public UserResponseDto(User user) {
        this.uid = user.getId();
        this.isAdmin = user.getIsAdmin();
        this.name = user.getName();
    }
}
