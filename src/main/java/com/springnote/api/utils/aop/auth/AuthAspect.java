package com.springnote.api.utils.aop.auth;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.springnote.api.dto.user.common.UserResponseDto;
import com.springnote.api.service.UserService;
import com.springnote.api.utils.context.UserContext;
import com.springnote.api.utils.exception.auth.AuthErrorCode;
import com.springnote.api.utils.exception.auth.AuthException;
import com.springnote.api.utils.exception.service.ServiceException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Order(2)
@Aspect
@RequiredArgsConstructor
@Component
public class AuthAspect {

    private final FirebaseAuth firebaseAuth;
    private final UserService userService;
    private final UserContext userContext;

    @Before("@annotation(enableAuth)")
    public void beforeRbacCheck(JoinPoint joinPoint, EnableAuth enableAuth) {

        if (enableAuth.authLevel() == AuthLevel.GUEST) return;

        var request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        var token = getToken(request);

        var uid = authenticate(token);

        var user = checkUserRole(uid, enableAuth.authLevel());

        userContext.setUser(user);
    }

    @Around("@annotation(enableAuth)")
    public Object aroundRbacCheck(ProceedingJoinPoint joinPoint, EnableAuth enableAuth) throws Throwable {
        // 헤더 값 검증 이후 원래 메소드 실행
        return joinPoint.proceed();
    }

    private String getToken(HttpServletRequest request) {
        var tokenHeader = request.getHeader("Authorization");

        if (tokenHeader == null) //TODO : throw auth exception
            throw new AuthException(AuthErrorCode.NOT_FOUND, "인증 토큰을 찾을 수 없습니다.");

        if (!tokenHeader.startsWith("Bearer "))   //TODO : throw auth exception
            throw new AuthException(AuthErrorCode.NOT_FOUND, "인증 토큰의 형식이 올바르지 않습니다. Bearer 토큰 형식인지 다시 확인하세요.");

        return tokenHeader.substring(7);
    }

    private String authenticate(String token) {
        try {
            var firebaseToken = firebaseAuth.verifyIdToken(token, true);
            return firebaseToken.getUid();
        } catch (FirebaseAuthException e) {
            throw new AuthException(AuthErrorCode.AUTH_FAIL, "해당 토큰 검증에 실패했습니다. 올바른 토큰인지 다시 확인하세요.");
        } catch (Exception e) {
            throw new AuthException(AuthErrorCode.UNKNOWN_ERROR, "알 수 없는 오류가 발생했습니다. 다시 시도해주세요.");
        }
    }

    private UserResponseDto checkUserRole(String uid, AuthLevel authLevel) {
        UserResponseDto user;
        try {
            user = userService.findUserById(uid);
        } catch (ServiceException e) {
            throw new AuthException(AuthErrorCode.NOT_FOUND, "해당 유저를 찾을 수 없습니다.");
        }

        if (authLevel == AuthLevel.ADMIN) {
            if (!user.isAdmin())
                throw new AuthException(AuthErrorCode.AUTH_FAIL, "해당 API에 접근할 권한이 없습니다.");
        }

        return user;

    }
}
