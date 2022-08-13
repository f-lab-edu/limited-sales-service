package com.limited.sales.utils;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
public class HttpResponse<T> {
  @NotBlank(message = "코드가 존재하지 않습니다.")
  @Pattern(regexp = "\\d{3}")
  private final int code;

  @NotBlank(message = "메세지가 존재하지 않습니다.")
  private final String message;

  private final T data;

  @Builder
  HttpResponse(int code, String message, T data) {
    this.code = code;
    this.message = message;
    this.data = data;
  }

  public static <T> HttpResponse<T> toResponse(int code, String message) {
    return toResponse(code, message, null);
  }

  public static <T> HttpResponse<T> toResponse(int code, String message, T data) {
    return HttpResponse.<T>builder().code(code).message(message).data(data).build();
  }
}
