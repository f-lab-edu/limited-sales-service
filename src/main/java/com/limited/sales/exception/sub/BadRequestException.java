package com.limited.sales.exception.sub;

public final class BadRequestException extends RuntimeException {
  public BadRequestException(String message) {
    super(message);
  }
}
