package com.limited.sales.token;

import com.limited.sales.config.LazyObjectHolder;
import com.limited.sales.exception.sub.TokenException;
import com.limited.sales.redis.RedisService;
import com.limited.sales.user.vo.User;
import com.limited.sales.utils.JwtProperties;
import com.limited.sales.utils.JwtUtils;
import com.limited.sales.utils.JwtValidationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public final class TokenServiceImpl implements TokenService {
  private final RedisService redisService;

  @Override
  public String reissue(@NotNull final User user) {
    final String refreshToken = redisService.getValue(user.getEmail());

    Optional.ofNullable(refreshToken)
        .filter(JwtValidationUtils::isRefreshTokenValid)
        .orElseThrow(
            () -> {
              throw new TokenException("리프레쉬 토큰이 정상적이지 않습니다.");
            });

    final String token = JwtProperties.TOKEN_PREFIX + JwtUtils.createAccessToken(user);

    final TokenVo tokenVo = TokenVo.builder().accessToken(token).build();
    return LazyObjectHolder.getGson().toJson(tokenVo);
  }

  @Override
  public void deleteRefreshToken(@NotNull final User user) {
    final String refreshToken = redisService.getValue(user.getEmail());

    Optional.ofNullable(refreshToken)
        .filter(JwtValidationUtils::isRefreshTokenValid)
        .orElseThrow(
            () -> {
              throw new TokenException("재로그인이 필요합니다.");
            });

    redisService.deleteValue(user.getEmail());
  }

  @Override
  public void blacklistAccessToken(@NotNull final User user, @NotNull final String authorization) {
    Optional.ofNullable(authorization)
        .filter(JwtValidationUtils::hasValidJwtToken)
        .orElseThrow(
            () -> {
              throw new TokenException("엑세스 토큰이 존재하지 않거나 올바르지 않습니다.");
            });

    final String replacePrefixToken = JwtUtils.replaceTokenPrefix(authorization);
    redisService.setValue(user.getEmail() + JwtProperties.BLACKLIST_POSTFIX, replacePrefixToken);
  }
}
