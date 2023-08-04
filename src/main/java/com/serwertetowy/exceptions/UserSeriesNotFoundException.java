package com.serwertetowy.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Watchlist item with given id not found")
public class UserSeriesNotFoundException extends RuntimeException {
    @Override
    public String getMessage() {
        return "Watchlist item with given id not found";
    }
}
