package com.hcmute.myanime.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NO_CONTENT)
public class ResourceDeleteException extends RuntimeException {
    public ResourceDeleteException(String message) {
        super(message);
    }
}
