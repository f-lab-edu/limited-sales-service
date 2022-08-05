package com.limited.sales.exception;

import com.limited.sales.exception.sub.*;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public final class GeneralExceptionHandler {
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ExceptionHandler({RuntimeException.class, LoginException.class})
  public ExceptionVo runTime(final RuntimeException e) {
    e.printStackTrace();
    return ExceptionVo.builder().msg(e.getLocalizedMessage()).code(e.getClass().getName()).build();
  }

  @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
  @ExceptionHandler({HttpMediaTypeException.class})
  public ExceptionVo notSupported(final HttpMediaTypeException e) {
    e.printStackTrace();
    return ExceptionVo.builder().msg(e.getLocalizedMessage()).code(e.getClass().getName()).build();
  }

  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  @ExceptionHandler({AccessDeniedException.class})
  public ExceptionVo accessIsDenied(final AccessDeniedException e) {
    e.printStackTrace();
    return ExceptionVo.builder().msg(e.getLocalizedMessage()).code(e.getClass().getName()).build();
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler({
    NotAllowedClaimException.class,
    TokenException.class,
    DuplicatedIdException.class,
    BadRequestException.class,
    NoValidUserException.class
  })
  public ExceptionVo badRequest(final RuntimeException e) {
    e.printStackTrace();
    return ExceptionVo.builder().msg(e.getLocalizedMessage()).code(e.getClass().getName()).build();
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ExceptionVo processValidationError(final MethodArgumentNotValidException exception) {
    final BindingResult bindingResult = exception.getBindingResult();

    final StringBuilder builder = new StringBuilder();
    for (FieldError fieldError : bindingResult.getFieldErrors()) {
      builder
          .append("[")
          .append(fieldError.getField())
          .append("](은)는 ")
          .append(fieldError.getDefaultMessage())
          .append(" 입력된 값: [")
          .append(fieldError.getRejectedValue())
          .append("]");
    }
    return ExceptionVo.builder().msg(builder.toString()).build();
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler({MissingRequestHeaderException.class})
  public ExceptionVo missingRequestHeader(final MissingRequestHeaderException e) {
    e.printStackTrace();
    return ExceptionVo.builder()
        .msg("누락된 헤더가 존재합니다. 다시 시도해주세요.")
        .code(e.getClass().getName())
        .build();
  }

  /** ExceptionVo JSON API 형태를 구성하기 위한 Vo */
  @Getter
  @Builder
  private static class ExceptionVo {
    private String msg;
    private String code;
  }
}
