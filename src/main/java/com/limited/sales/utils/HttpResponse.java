package com.limited.sales.utils;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import javax.validation.constraints.NotBlank;

@Getter
public class HttpResponse<T> {
  @NotBlank(message = "응답상태가 존재하지 않습니다.")
  private final HttpStatus status;

  @NotBlank(message = "메세지가 존재하지 않습니다.")
  private final String message;

  private final T data;

  @Builder
  HttpResponse(HttpStatus status, String message, T data) {
    this.status = status;
    this.message = message;
    this.data = data;
  }

  public static <T> HttpResponse<T> toResponse(HttpStatus status, String message) {
    return toResponse(status, message, null);
  }

  public static <T> HttpResponse<T> toResponse(HttpStatus status, String message, T data) {
    return HttpResponse.<T>builder().status(status).message(message).data(data).build();
  }
}
