package com.limited.sales.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@NoArgsConstructor
public class ExceptionVo<T> {
    private HttpStatus status;
    private String message;
    private T data;

    @Builder
    public ExceptionVo(HttpStatus status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
