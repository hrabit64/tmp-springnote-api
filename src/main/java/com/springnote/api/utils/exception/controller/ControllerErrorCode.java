package com.springnote.api.utils.exception.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ControllerErrorCode {
    NOT_VALID(HttpStatus.BAD_REQUEST, "Not Valid", "CONTROLLER-001");

    private final HttpStatus httpStatus;
    private final String title;
    private final String errorCode;
}
