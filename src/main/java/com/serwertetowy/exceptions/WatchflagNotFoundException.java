package com.serwertetowy.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Watchflag with given id not found")
public class WatchflagNotFoundException extends RuntimeException {
    @Override
    public String getMessage() {
        return "Watchflag with given id not found";
    }
}