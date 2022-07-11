package com.limited.sales.exception;

import com.limited.sales.exception.sub.LoginException;
import com.limited.sales.exception.sub.NoValidUserException;
import com.limited.sales.exception.sub.SignException;
import com.limited.sales.exception.sub.TokenException;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public final class ExceptionController {

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = {RuntimeException.class})
    public ExceptionVo runTime(final RuntimeException e){
        e.printStackTrace();
        return ExceptionVo.builder().msg(e.getLocalizedMessage()).code(e.getClass().getName()).build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = {TokenException.class})
    public ExceptionVo token(final TokenException e){
        e.printStackTrace();
        return ExceptionVo.builder().msg(e.getLocalizedMessage()).code(e.getClass().getName()).build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = {SignException.class})
    public ExceptionVo sign(final SignException e){
        e.printStackTrace();
        return ExceptionVo.builder().msg(e.getLocalizedMessage()).code(e.getClass().getName()).build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = {NoValidUserException.class})
    public ExceptionVo noValidUser(final NoValidUserException e){
        e.printStackTrace();
        return ExceptionVo.builder().msg(e.getLocalizedMessage()).code(e.getClass().getName()).build();
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = {LoginException.class})
    public ExceptionVo login(final LoginException e){
        e.printStackTrace();
        return ExceptionVo.builder().msg(e.getLocalizedMessage()).code(e.getClass().getName()).build();
    }

    @Getter
    @Builder
    private static final class ExceptionVo {
        private String msg;
        private String code;
    }
}
