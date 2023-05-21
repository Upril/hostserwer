package com.serwertetowy.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Tag with given id not found")
public class TagNotFoundException extends RuntimeException {
}