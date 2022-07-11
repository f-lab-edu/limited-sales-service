package com.limited.sales.user.vo;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {

    @NotEmpty(message = "이메일은 빈값 일 수 없습니다")
    @NotNull(message = "이메일은 Null 일 수 없습니다")
   // @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$", message = "이메일 형식에 맞지 않습니다.")
    private String userEmail;

    @NotEmpty(message = "비밀번호는 빈값 일 수 없습니다")
    @NotNull(message = "비밀번호는 Null 일 수 없습니다")
    private String userPassword;

    @NotEmpty(message = "휴대폰 번호는 빈값 일 수 없습니다")
    @NotNull(message = "휴대폰 번호는 Null 일 수 없습니다")
    private String userCellphone;

    private String userRole;
    private String useYn;
    private String refreshToken;

}
