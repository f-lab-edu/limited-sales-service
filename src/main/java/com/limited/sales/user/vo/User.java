package com.limited.sales.user.vo;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User {
  @NotBlank(message = "{user.email.null}")
  private String email;

  @NotBlank(message = "{user.password.null}")
  private String password;

  private String cellphone;
  private String role;
  private Status status;

  /** 사용자 아이디 상태 정보 */
  public enum Status {
    Y("Y"),
    N("N");

    private final String value;

    Status(String value) {
      this.value = value;
    }

    public String getValue() {
      return value;
    }
  }
}
