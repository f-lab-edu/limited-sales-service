package com.limited.sales.user.vo;

import lombok.*;

import javax.validation.constraints.NotEmpty;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User {
  private String userEmail;
  private String userPassword;
  private String userCellphone;
  private String userRole;
  private String useYn;
}
