package com.springnote.api.utils.context;

import com.springnote.api.dto.user.common.UserResponseDto;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;


@Setter
@Getter
@Component
@Scope(value="request", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UserContext {
    private String uid = null;
    private String name = null;
    private boolean isAdmin = false;

    public void setUser(UserResponseDto user){
        this.uid = user.getUid();
        this.name = user.getName();
        this.isAdmin = user.isAdmin();
    }

    public boolean isExist(){
        return this.uid != null;
    }
}
