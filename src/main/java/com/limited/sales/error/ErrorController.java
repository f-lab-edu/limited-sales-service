package com.limited.sales.error;

import com.limited.sales.error.exception.BadRequestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ErrorController {

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(BadRequestException.class)
  public ErrorMsg BadRequestException(BadRequestException e) {
    log.error("[exceptionHandler] ex", e);
    return new ErrorMsg("BAD", e.getMessage());
  }
}
