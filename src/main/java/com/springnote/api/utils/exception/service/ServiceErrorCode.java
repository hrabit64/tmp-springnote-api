package com.springnote.api.utils.exception.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ServiceErrorCode {

    NOT_FOUND(HttpStatus.NOT_FOUND, "Item Not Found", "SERVICE-001"),
    UNKNOWN_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Unknow Error", "SERVICE-002"),
    ALREADY_EXIST(HttpStatus.CONFLICT, "Already Exist", "SERVICE-003"),
    NOT_VALID(HttpStatus.BAD_REQUEST, "NOT VALID", "SERVICE-004");

    private final HttpStatus httpStatus;
    private final String title;
    private final String errorCode;
}
