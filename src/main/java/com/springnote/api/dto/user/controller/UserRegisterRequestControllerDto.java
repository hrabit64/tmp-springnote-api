package com.springnote.api.dto.user.controller;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.springnote.api.dto.user.service.UserCreateRequestServiceDto;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@EqualsAndHashCode
@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class UserRegisterRequestControllerDto {


    @NotEmpty(message = "이름은 필수값입니다.")
    @Length(min = 2, max = 10, message = "이름은 2자 이상 10자 이하입니다.")
    private String name;

    public UserCreateRequestServiceDto toServiceDto(String uid){
        return UserCreateRequestServiceDto.builder()
                .uid(uid)
                .name(name)
                .isAdmin(false)
                .build();
    }
}
