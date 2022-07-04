package com.limited.sales.user.vo;

import lombok.Data;

@Data
public class User {

    private String userEmail;
    private String userPassword;
    private String userCellphone;
    private String userRole;
    private String useYn;
    private String refreshToken;

}
