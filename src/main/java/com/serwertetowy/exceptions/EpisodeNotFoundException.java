package com.serwertetowy.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Episode with given id not found")
public class EpisodeNotFoundException extends RuntimeException{
    @Override
    public String getMessage() {
        return "Episode with given id not found";
    }
}