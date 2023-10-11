package com.springnote.api.utils.context;

import com.springnote.api.dto.user.common.UserResponseDto;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


@Setter
@Getter
@Component
@Scope("request")
public class UserContext {
    private String uid = null;
    private String name = null;

    public void setUser(UserResponseDto user){
        this.uid = user.getUid();
        this.name = user.getName();
    }
}
