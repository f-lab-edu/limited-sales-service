package com.limited.sales.exception;

import com.google.gson.Gson;
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
public class ExceptionController {
    private final Gson gson = new Gson();

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = {RuntimeException.class})
    public String runTime(final RuntimeException e){
        return gson.toJson(ExceptionVo.builder()
                .msg(e.getLocalizedMessage())
                .code(e.getClass().getName())
                .build());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = {TokenException.class})
    public String token(final TokenException e){
        return gson.toJson(ExceptionVo.builder()
                .msg(e.getLocalizedMessage())
                .code(e.getClass().getName())
                .build());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = {SignException.class})
    public String sign(final SignException e){
        return gson.toJson(ExceptionVo.builder()
                .msg(e.getLocalizedMessage())
                .code(e.getClass().getName())
                .build());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = {NoValidUserException.class})
    public String noValidUser(final NoValidUserException e){
        return gson.toJson(ExceptionVo.builder()
                .msg(e.getLocalizedMessage())
                .code(e.getClass().getName())
                .build());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = {LoginException.class})
    public String login(final LoginException e){
        return gson.toJson(ExceptionVo.builder()
                .msg(e.getLocalizedMessage())
                .code(e.getClass().getName())
                .build());
    }

    @Getter
    @Builder
    private static final class ExceptionVo {
        private String msg;
        private String code;
    }
}
