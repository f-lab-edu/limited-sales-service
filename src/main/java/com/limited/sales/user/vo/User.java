package com.limited.sales.user.vo;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User {
  @NotBlank(message = "이메일을 입력하지 않았습니다.")
  private String userEmail;
  @NotBlank(message = "비밀번호를 입력하지 않았습니다.")
  private String userPassword;
  private String userCellphone;
  private String userRole;
  private String useYn;
}
