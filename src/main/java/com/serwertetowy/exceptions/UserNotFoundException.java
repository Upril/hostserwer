package com.serwertetowy.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "User with given id not found")
public class UserNotFoundException extends RuntimeException {
    @Override
    public String getMessage() {
        return "User with given id not found";
    }
}
