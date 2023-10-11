package com.springnote.api.utils.aop.auth;

import java.lang.annotation.*;

@Inherited
@Retention(value = RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface EnableAuth {
    AuthLevel authLevel() default AuthLevel.GUEST;
}
