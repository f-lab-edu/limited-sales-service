package com.limited.sales.exception.sub;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NotAllowedClaimException extends RuntimeException {
  public NotAllowedClaimException(String message) {
    super(message);
  }
}
