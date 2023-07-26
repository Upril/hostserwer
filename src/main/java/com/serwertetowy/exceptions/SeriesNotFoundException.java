package com.serwertetowy.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Series with given id not found")
public class SeriesNotFoundException extends RuntimeException {
    @Override
    public String getMessage() {
        return "Series with given id not found";
    }
}
