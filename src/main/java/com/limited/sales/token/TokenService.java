package com.limited.sales.token;

import com.limited.sales.user.vo.User;

import javax.validation.constraints.NotNull;

public interface TokenService {
  String reissue(@NotNull final User user);

  void refreshTokenDelete(@NotNull final User user);

  void accessTokenBlack(@NotNull final User user, @NotNull final String authorization);
}
