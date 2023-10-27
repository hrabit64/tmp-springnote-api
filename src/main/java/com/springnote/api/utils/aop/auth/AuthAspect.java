package com.springnote.api.utils.aop.auth;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.springnote.api.dto.user.common.UserResponseDto;
import com.springnote.api.service.FirebaseService;
import com.springnote.api.service.UserService;
import com.springnote.api.utils.context.UserContext;
import com.springnote.api.utils.exception.auth.AuthErrorCode;
import com.springnote.api.utils.exception.auth.AuthException;
import com.springnote.api.utils.exception.service.ServiceErrorCode;
import com.springnote.api.utils.exception.service.ServiceException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Slf4j
@Order(2)
@Aspect
@RequiredArgsConstructor
@Component
public class AuthAspect {

    private final FirebaseService firebaseService;
    private final UserContext userContext;

    @Before("@annotation(enableAuth)")
    public void beforeRbacCheck(JoinPoint joinPoint, EnableAuth enableAuth) {

        if (enableAuth.authLevel() == AuthLevel.GUEST) return;

        var request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        var token = getToken(request);

        var user = authenticate(token);

        checkUserRole(user.isAdmin(), enableAuth.authLevel());

        userContext.setUser(user);
    }

    @Around("@annotation(enableAuth)")
    public Object aroundRbacCheck(ProceedingJoinPoint joinPoint, EnableAuth enableAuth) throws Throwable {
        // 헤더 값 검증 이후 원래 메소드 실행
        return joinPoint.proceed();
    }

    private String getToken(HttpServletRequest request) {
        var tokenHeader = request.getHeader("Authorization");

        if (tokenHeader == null)
            throw new AuthException(AuthErrorCode.NOT_FOUND, "인증 토큰을 찾을 수 없습니다.");

        if (!tokenHeader.startsWith("Bearer "))
            throw new AuthException(AuthErrorCode.NOT_FOUND, "인증 토큰의 형식이 올바르지 않습니다. Bearer 토큰 형식인지 다시 확인하세요.");

        return tokenHeader.substring(7);
    }

    private UserResponseDto authenticate(String token) {
        try {
            return firebaseService.findUserByToken(token);
        } catch (ServiceException e) {
            if (e.getErrorCode() == ServiceErrorCode.NOT_FOUND) {
                throw new AuthException(AuthErrorCode.AUTH_FAIL, "인증 토큰에 실패했습니다. 만료되었거나 유효하지 않은 토큰입니다.");
            } else {
                throw new AuthException(AuthErrorCode.UNKNOWN_ERROR, "알 수 없는 오류가 발생했습니다. 다시 시도해주세요.");
            }
        } catch (Exception e) {
            log.debug(e.getMessage());
            throw new AuthException(AuthErrorCode.UNKNOWN_ERROR, "알 수 없는 오류가 발생했습니다. 다시 시도해주세요.");
        }
    }

    private void checkUserRole(boolean isAdmin, AuthLevel authLevel) {

        if (authLevel == AuthLevel.ADMIN) {
            if (!isAdmin)
                throw new AuthException(AuthErrorCode.AUTH_FAIL, "해당 API에 접근할 권한이 없습니다.");
        }

    }
}
