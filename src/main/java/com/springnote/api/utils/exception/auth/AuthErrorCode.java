package com.springnote.api.utils.exception.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum AuthErrorCode {

    NOT_FOUND(HttpStatus.UNAUTHORIZED,"Auth Request Error", "AUTH-001"),
    AUTH_FAIL(HttpStatus.FORBIDDEN,"Cannot Access", "AUTH-002"),
    UNKNOWN_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Unknown Error", "AUTH-003");

    private final HttpStatus httpStatus;
    private final String title;
    private final String errorCode;
}
