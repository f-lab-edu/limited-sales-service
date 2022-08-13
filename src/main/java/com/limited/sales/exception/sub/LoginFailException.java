package com.limited.sales.exception.sub;

import org.springframework.security.authentication.BadCredentialsException;

public class LoginFailException extends BadCredentialsException {
    public LoginFailException(String msg) {
        super(msg);
    }

    public LoginFailException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
