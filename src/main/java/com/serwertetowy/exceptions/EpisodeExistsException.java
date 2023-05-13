package com.serwertetowy.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Episode with given id already exists")
public class EpisodeExistsException extends RuntimeException{}
