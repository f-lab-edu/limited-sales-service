package com.limited.sales.exception;

import com.limited.sales.exception.sub.*;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.HttpMediaTypeException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;

/** Exception 발생시 JSON 형태로 변경해 전달해주는 역할 */
@Slf4j
@RestControllerAdvice
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public final class ExceptionController {
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ExceptionHandler({RuntimeException.class})
  public ExceptionVo runTime(final RuntimeException e) {
    e.printStackTrace();
    return ExceptionVo.builder().msg(e.getLocalizedMessage()).code(e.getClass().getName()).build();
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler({NotAllowedClaimException.class})
  public ExceptionVo notAllowedClaim(final NotAllowedClaimException e) {
    e.printStackTrace();
    return ExceptionVo.builder().msg(e.getLocalizedMessage()).code(e.getClass().getName()).build();
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler({TokenException.class})
  public ExceptionVo token(final TokenException e) {
    e.printStackTrace();
    return ExceptionVo.builder().msg(e.getLocalizedMessage()).code(e.getClass().getName()).build();
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler({DuplicatedIdException.class})
  public ExceptionVo duplicated(final DuplicatedIdException e) {
    e.printStackTrace();
    return ExceptionVo.builder().msg(e.getLocalizedMessage()).code(e.getClass().getName()).build();
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler({BadRequestException.class})
  public ExceptionVo badRequest(final BadRequestException e) {
    e.printStackTrace();
    return ExceptionVo.builder().msg(e.getLocalizedMessage()).code(e.getClass().getName()).build();
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler({NoValidUserException.class})
  public ExceptionVo noValidUser(final NoValidUserException e) {
    e.printStackTrace();
    return ExceptionVo.builder().msg(e.getLocalizedMessage()).code(e.getClass().getName()).build();
  }

  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ExceptionHandler({LoginException.class})
  public ExceptionVo login(final LoginException e) {
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
  @ExceptionHandler({MethodArgumentNotValidException.class})
  public ExceptionVo methodArgumentNot(final MethodArgumentNotValidException e) {
    e.printStackTrace();
    return ExceptionVo.builder()
        .msg(e.getBindingResult().getAllErrors().get(0).getDefaultMessage())
        .code(e.getClass().getName())
        .build();
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler({MissingRequestHeaderException.class})
  public ExceptionVo missingRequestHeader(final MissingRequestHeaderException e) {
    e.printStackTrace();
    e.getLocalizedMessage();
    e.getMessage();
    return ExceptionVo.builder()
        .msg("test")
        .code(e.getClass().getName())
        .build();
  }

  /** ExceptionVo JSON API 형태를 구성하기 위한 Vo */
  @Getter
  @Builder
  private static final class ExceptionVo {
    private String msg;
    private String code;
  }
}
