package com.limited.sales.token;

import lombok.Builder;
import lombok.Getter;

@Getter
class TokenVo {
  private final String accessToken;
  private final String refreshToken;

  @Builder
  public TokenVo(String accessToken, String refreshToken) {
    this.accessToken = accessToken;
    this.refreshToken = refreshToken;
  }
}
