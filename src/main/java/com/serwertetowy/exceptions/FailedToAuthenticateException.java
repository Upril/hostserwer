package com.serwertetowy.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Failed to authenticate the user")
public class FailedToAuthenticateException extends RuntimeException {
    @Override
    public String getMessage() {
        return "Failed to authenticate the user";
    }
}
