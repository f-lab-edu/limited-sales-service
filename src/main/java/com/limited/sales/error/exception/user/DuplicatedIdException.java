package com.limited.sales.error.exception.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class DuplicatedIdException extends RuntimeException {
    public DuplicatedIdException(String msg) {
        super(msg);
    }
}
