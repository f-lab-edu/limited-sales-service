package com.limited.sales.token;

import com.limited.sales.config.Constant;
import com.limited.sales.exception.sub.TokenException;
import com.limited.sales.redis.RedisService;
import com.limited.sales.user.vo.User;
import com.limited.sales.utils.JwtProperties;
import com.limited.sales.utils.JwtUtils;
import com.limited.sales.utils.JwtValidationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.concurrent.ConcurrentMap;

@Service
@RequiredArgsConstructor
public final class TokenServiceImpl implements TokenService {
  private final RedisService redisService;
  private final ConcurrentMap<String, String> BLACK_LIST = TokenBlacklist.getInstance();

  @Override
  public String reissue(@NotNull final User user) {
    final String refreshToken = redisService.getValue(user.getUserEmail());

    if (!JwtValidationUtils.isRefreshTokenValid(refreshToken)){
      throw new TokenException("억세스 토큰 재발급 도중 리프레쉬 토큰이 정상적이지 않습니다.");
    }
    final String token = JwtProperties.TOKEN_PREFIX + JwtUtils.createAccessToken(user);

    final TokenVo tokenVo = TokenVo.builder().accessToken(token).build();
    return Constant.getGson().toJson(tokenVo);
  }

  @Override
  public void deleteRefreshToken(@NotNull final User user) {
    final String userEmail = user.getUserEmail();
    final String refreshToken = redisService.getValue(user.getUserEmail());

    if (!JwtValidationUtils.isRefreshTokenValid(refreshToken)) {
      throw new TokenException("재로그인이 필요합니다.");
    }

    redisService.deleteValue(userEmail);
  }

  @Override
  public void blacklistAccessToken(@NotNull final User user,
                                   @NotNull final String authorization) {
    if (JwtValidationUtils.hasValidJwtToken(authorization)) {
      throw new TokenException("엑세스 토큰이 존재하지 않거나 올바르지 않습니다.");
    }
    final String prefixAuthorization = JwtUtils.replaceTokenPrefix(authorization);
    BLACK_LIST.put(user.getUserEmail(), prefixAuthorization);
  }
}
