package com.springnote.api.utils.aop.request;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.springnote.api.dto.user.common.UserResponseDto;
import com.springnote.api.service.UserService;
import com.springnote.api.utils.aop.auth.AuthLevel;
import com.springnote.api.utils.aop.auth.EnableAuth;
import com.springnote.api.utils.context.RequestIdContext;
import com.springnote.api.utils.exception.auth.AuthErrorCode;
import com.springnote.api.utils.exception.auth.AuthException;
import com.springnote.api.utils.exception.service.ServiceException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.UUID;

@Order(1)
@Aspect
@RequiredArgsConstructor
@Component
public class RequestIdAspect {

    private final RequestIdContext requestIdContext;

    @Pointcut("within(org.springframework.web.bind.annotation.RestController)")
    public void targetControllerMethods() {
    }

    @Before("targetControllerMethods()")
    public void beforeControllerMethod() {
        requestIdContext.setId(UUID.randomUUID().toString());
    }

}
