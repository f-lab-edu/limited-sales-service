package com.limited.sales.exception;

import com.limited.sales.exception.sub.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
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
  public ExceptionVo<Void> runTime(final RuntimeException e) {
    log.error(e.toString());
    e.printStackTrace();
    return ExceptionVo.<Void>builder()
        .message(e.getLocalizedMessage())
        .status(HttpStatus.INTERNAL_SERVER_ERROR)
        .build();
  }

  @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
  @ExceptionHandler({HttpMediaTypeException.class})
  public ExceptionVo<Void> notSupported(final HttpMediaTypeException e) {
    log.error(e.toString());
    e.printStackTrace();
    return ExceptionVo.<Void>builder()
        .message(e.getLocalizedMessage())
        .status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
        .build();
  }

  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  @ExceptionHandler({
    AccessDeniedException.class,
    LoginFailException.class,
    InternalAuthenticationServiceException.class,
  })
  public ExceptionVo<Void> unauthorized(final RuntimeException e) {
    log.error(e.toString());
    e.printStackTrace();
    return ExceptionVo.<Void>builder()
        .message(e.getLocalizedMessage())
        .status(HttpStatus.UNAUTHORIZED)
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
    MethodArgumentTypeMismatchException.class,
    HttpMessageNotReadableException.class,
    RedisBadArgumentException.class,
    IllegalArgumentException.class,
    HttpRequestMethodNotSupportedException.class,
  })
  public ExceptionVo<Void> badRequest(final Exception e) {
    log.error(e.toString());
    e.printStackTrace();
    final ExceptionVo<Void> exceptionVo =
        ExceptionVo.<Void>builder()
            .status(HttpStatus.BAD_REQUEST)
            .message(e.getLocalizedMessage())
            .build();

    if (e instanceof MethodArgumentTypeMismatchException) {
      exceptionVo.setMessage("숫자만 입력할 수 있습니다.");
    }

    if(e instanceof HttpRequestMethodNotSupportedException) {
      exceptionVo.setMessage("해당 메소드는 지원하지 않습니다.");
    }

    return exceptionVo;
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ExceptionVo<Void> processValidationError(final MethodArgumentNotValidException e) {
    log.error(e.toString());
    e.printStackTrace();
    return ExceptionVo.<Void>builder()
        .message(
            e.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining()))
        .status(HttpStatus.BAD_REQUEST)
        .build();
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler({ConstraintViolationException.class})
  public ExceptionVo<Void> constraintViolation(final ConstraintViolationException e) {
    log.error(e.toString());
    e.printStackTrace();
    return ExceptionVo.<Void>builder()
        .message(
            e.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining()))
        .status(HttpStatus.BAD_REQUEST)
        .build();
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler({MissingRequestHeaderException.class})
  public ExceptionVo<Void> missingRequestHeader(final MissingRequestHeaderException e) {
    log.error(e.toString());
    e.printStackTrace();
    return ExceptionVo.<Void>builder()
        .message("누락된 헤더가 존재합니다. 다시 시도해주세요.")
        .status(HttpStatus.BAD_REQUEST)
        .build();
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler({NumberFormatException.class})
  public ExceptionVo<Void> numberFormat(final NumberFormatException e) {
    log.error(e.toString());
    e.printStackTrace();
    return ExceptionVo.<Void>builder()
        .message("숫자만 입력할 수 있습니다.")
        .status(HttpStatus.BAD_REQUEST)
        .build();
  }
}
