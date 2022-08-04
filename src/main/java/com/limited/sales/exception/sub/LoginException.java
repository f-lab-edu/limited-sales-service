package com.limited.sales.exception.sub;

import org.springframework.security.core.AuthenticationException;

public final class LoginException extends AuthenticationException {
  public LoginException(String message) {
    super(message);
  }
}
