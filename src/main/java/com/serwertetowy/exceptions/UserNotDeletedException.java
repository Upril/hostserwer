package com.serwertetowy.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "User with given id is not deleted")
public class UserNotDeletedException extends RuntimeException{
    @Override
    public String getMessage() {
        return "User with given id is not deleted";
        //or whatever message will be needed
    }
}
