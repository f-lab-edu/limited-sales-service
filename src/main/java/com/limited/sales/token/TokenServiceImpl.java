package com.limited.sales.token;

import com.limited.sales.exception.sub.LoginException;
import com.limited.sales.exception.sub.TokenException;
import com.limited.sales.redis.RedisService;
import com.limited.sales.user.vo.User;
import com.limited.sales.utils.JwtProperties;
import com.limited.sales.utils.JwtUtils;
import com.limited.sales.utils.JwtValidationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

@Service
@Validated
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {
  private final RedisService<String> redisService;

  @Override
  public TokenVo reissue(final User user) {
    if (!JwtValidationUtils.isRefreshTokenValid(redisService.getValue(user.getEmail()))) {
      throw new TokenException("리프레쉬 토큰이 정상적이지 않습니다.");
    }

    return TokenVo.builder()
        .accessToken(JwtProperties.TOKEN_PREFIX + JwtUtils.createAccessToken(user))
        .build();
  }

  @Override
  public void deleteRefreshToken(final User user) {
    if (!JwtValidationUtils.isRefreshTokenValid(redisService.getValue(user.getEmail()))) {
      throw new LoginException("재 로그인이 필요합니다.");
    }

    redisService.deleteValue(user.getEmail());
  }

  @Override
  public void blacklistAccessToken(
      final User user, @NotBlank(message = "토큰이 존재하지 않습니다.") final String authorization) {
    if (JwtValidationUtils.hasValidJwtTokenNull(authorization)) {
      throw new TokenException("엑세스 토큰이 올바르지 않습니다.");
    }

    redisService.setValue(
        user.getEmail() + JwtProperties.BLACKLIST_POSTFIX,
        JwtUtils.replaceTokenPrefix(authorization));
  }
}
