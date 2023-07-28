package com.serwertetowy.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Rating with given id not found")
public class RatingNotFoundException extends RuntimeException{
    @Override
    public String getMessage() {
        return "Rating with given id not found";
    }
}
