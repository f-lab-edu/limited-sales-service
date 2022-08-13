package com.limited.sales.exception;

import com.limited.sales.exception.sub.*;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public final class GeneralExceptionHandler {
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ExceptionHandler({RuntimeException.class, LoginException.class})
  public ExceptionVo runTime(final RuntimeException e) {
    log.error(e.toString());
    e.printStackTrace();
    return ExceptionVo.builder()
        .message(e.getLocalizedMessage())
        .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
        .build();
  }

  @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
  @ExceptionHandler({HttpMediaTypeException.class})
  public ExceptionVo notSupported(final HttpMediaTypeException e) {
    log.error(e.toString());
    e.printStackTrace();
    return ExceptionVo.builder()
        .message(e.getLocalizedMessage())
        .code(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value())
        .build();
  }

  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  @ExceptionHandler({AccessDeniedException.class, LoginFailException.class})
  public ExceptionVo unauthorized(final RuntimeException e) {
    log.error(e.toString());
    e.printStackTrace();
    return ExceptionVo.builder()
        .message(e.getLocalizedMessage())
        .code(HttpStatus.UNAUTHORIZED.value())
        .build();
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler({
    NotAllowedClaimException.class,
    TokenException.class,
    DuplicatedIdException.class,
    BadRequestException.class,
    NoValidUserException.class,
    DuplicateKeyException.class,
  })
  public ExceptionVo badRequest(final RuntimeException e) {
    log.error(e.toString());
    e.printStackTrace();
    return ExceptionVo.builder()
        .message(e.getLocalizedMessage())
        .code(HttpStatus.BAD_REQUEST.value())
        .build();
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ExceptionVo processValidationError(final MethodArgumentNotValidException e) {
    log.error(e.toString());
    e.printStackTrace();
    return ExceptionVo.builder()
        .message(
            e.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining()))
        .code(HttpStatus.BAD_REQUEST.value())
        .build();
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler({ConstraintViolationException.class})
  public ExceptionVo constraintViolation(final ConstraintViolationException e) {
    log.error(e.toString());
    e.printStackTrace();
    return ExceptionVo.builder()
        .message(
            e.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining()))
        .code(HttpStatus.BAD_REQUEST.value())
        .build();
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler({MethodArgumentTypeMismatchException.class})
  public ExceptionVo methodArgumentTypeMismatch(final MethodArgumentTypeMismatchException e) {
    log.error(e.toString());
    e.printStackTrace();
    return ExceptionVo.builder()
        .message(e.getLocalizedMessage())
        .code(HttpStatus.BAD_REQUEST.value())
        .build();
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler({MissingRequestHeaderException.class})
  public ExceptionVo missingRequestHeader(final MissingRequestHeaderException e) {
    log.error(e.toString());
    e.printStackTrace();
    return ExceptionVo.builder()
        .message("누락된 헤더가 존재합니다. 다시 시도해주세요.")
        .code(HttpStatus.BAD_REQUEST.value())
        .build();
  }

  /** ExceptionVo JSON API 형태를 구성하기 위한 Vo */
  @Getter
  private static class ExceptionVo<T> {

    private final int code;
    private final String message;
    private final T data;

    @Builder
    public ExceptionVo(int code, String message, T data) {
      this.code = code;
      this.message = message;
      this.data = data;
    }
  }
}
