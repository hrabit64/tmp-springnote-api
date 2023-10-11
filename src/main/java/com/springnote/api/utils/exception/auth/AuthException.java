package com.springnote.api.utils.exception.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AuthException extends RuntimeException{
    private final AuthErrorCode errorCode;
    private final String message;
}
