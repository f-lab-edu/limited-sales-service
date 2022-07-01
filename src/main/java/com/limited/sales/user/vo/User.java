package com.limited.sales.user.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {
    private String userEmail;
    private String userPassword;
    private String userCellphone;
    private String userRole;
    private String useYn;
}
