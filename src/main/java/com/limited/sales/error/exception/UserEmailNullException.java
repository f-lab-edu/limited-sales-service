package com.limited.sales.error.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class UserEmailNullException extends RuntimeException {
    public UserEmailNullException(String msg) {
        super(msg);
    }
}
