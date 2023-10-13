package com.springnote.api.utils.context;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Setter
@Getter
@Component
@Scope("request")
public class RequestIdContext {
    private String id = null;
}
