package com.limited.sales.error.exception.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_ACCEPTABLE)
public class InvalidAuthenticationException extends RuntimeException {
  public InvalidAuthenticationException(String msg) {
    super(msg);
  }
}
