package com.limited.sales.user.vo;

import lombok.*;

import java.io.Serializable;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User implements Serializable {
  private String userEmail;
  private String userPassword;
  private String userCellphone;
  private String userRole;
  private String useYn;
}
