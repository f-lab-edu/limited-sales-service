package com.limited.sales.exception;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ExceptionVo<T> {
    private final HttpStatus status;
    private final String message;
    private final T data;

    @Builder
    public ExceptionVo(HttpStatus status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }
}
