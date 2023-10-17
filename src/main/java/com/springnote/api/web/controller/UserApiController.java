package com.springnote.api.web.controller;

import com.springnote.api.dto.user.common.UserResponseDto;
import com.springnote.api.dto.user.controller.UserRegisterRequestControllerDto;
import com.springnote.api.dto.user.controller.UserUpdateRequestControllerDto;
import com.springnote.api.service.FirebaseService;
import com.springnote.api.service.UserService;
import com.springnote.api.utils.aop.auth.AuthLevel;
import com.springnote.api.utils.aop.auth.EnableAuth;
import com.springnote.api.utils.context.UserContext;
import com.springnote.api.utils.modelAssembler.UserResponseDtoAssembler;
import com.springnote.api.utils.exception.auth.AuthErrorCode;
import com.springnote.api.utils.exception.auth.AuthException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.EntityModel;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Validated
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/user")
public class UserApiController {

    private final UserService userService;
    private final FirebaseService firebaseService;
    private final UserContext userContext;
    private final UserResponseDtoAssembler userResponseDtoAssembler;

    @PostMapping("/register")
    public EntityModel<UserResponseDto> registerUser(
            @RequestBody @Validated UserRegisterRequestControllerDto dto,
            @RequestHeader(value = "Authorization", required = true) String token) {

        //firebase에서는 access token역할을 하는 토큰을 id token이라고 부름.OIDC의 그것과는 다름.
        var idToken = getToken(token);
        var tokenInfo = firebaseService.decodeToken(idToken);
        var result = userService.createUser(dto.toServiceDto(tokenInfo.getUid()));
        return userResponseDtoAssembler.toModel(result);

    }

    //TODO : 자기 자신 조회시 그냥 User Context 리턴하도록 수정하기
    @EnableAuth(authLevel = AuthLevel.USER)
    @GetMapping("/{userId}")
    public EntityModel<UserResponseDto> getUser(@PathVariable String userId) {
        if (isSameUserOrAdmin(userId))
            return userResponseDtoAssembler.toModel(userService.findUserById(userId));
        else
            throw new AuthException(AuthErrorCode.AUTH_FAIL, "권한이 없습니다.");
    }

    @EnableAuth(authLevel = AuthLevel.USER)
    @DeleteMapping("/{userId}")
    public EntityModel<UserResponseDto> withdrawUser(@PathVariable String userId) {
        if (isSameUserOrAdmin(userId))
            return userResponseDtoAssembler.toModel(userService.deleteUser(userId));
        else
            throw new AuthException(AuthErrorCode.AUTH_FAIL, "권한이 없습니다.");
    }

    @EnableAuth(authLevel = AuthLevel.USER)
    @PutMapping("/{userId}")
    public EntityModel<UserResponseDto> updateUser(@PathVariable String userId, @Validated @RequestBody UserUpdateRequestControllerDto dto) {
        if (isSameUserOrAdmin(userId))
            return userResponseDtoAssembler.toModel(userService.updateUser(dto.toServiceDto(userId)));
        else
            throw new AuthException(AuthErrorCode.AUTH_FAIL, "권한이 없습니다.");
    }

    private String getToken(String token) {
        if (!token.startsWith("Bearer "))
            throw new AuthException(AuthErrorCode.NOT_FOUND, "인증 토큰의 형식이 올바르지 않습니다. Bearer 토큰 형식인지 다시 확인하세요.");

        return token.substring(7);
    }

    private boolean isSameUserOrAdmin(String userId) {
        return userContext.getUid().equals(userId) || userContext.isAdmin();
    }
}
