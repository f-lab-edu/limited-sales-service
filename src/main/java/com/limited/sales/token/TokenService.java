package com.limited.sales.token;

import com.google.gson.Gson;
import com.limited.sales.exception.sub.TokenException;
import com.limited.sales.redis.RedisService;
import com.limited.sales.user.vo.User;
import com.limited.sales.utils.JwtProperties;
import com.limited.sales.utils.JwtProvider;
import com.limited.sales.utils.JwtValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.concurrent.ConcurrentMap;

@Service
@RequiredArgsConstructor
public final class TokenService {
    private final JwtProvider jwtProvider = new JwtProvider();
    private final JwtValidation jwtValidation = new JwtValidation();
    private final RedisService redisService;
    private final ConcurrentMap<String, String> BLACK_LIST = TokenBlackListInit.getInstance();

    public String reissue(@NotNull final User user){
        final String refreshToken = redisService.getValues(user.getUserEmail());
        final Gson gson = new Gson();

        if(!jwtValidation.isValidationRefreshTokenCheck(refreshToken)) throw new TokenException("억세스 토큰 재발급 도중 리프레쉬 토큰이 정상적이지 않습니다.");
        final String token = JwtProperties.TOKEN_PREFIX + jwtProvider.createAccessTokenMethod(user);

        final TokenVo tokenVo = TokenVo.builder().accessToken(token).build();
        return gson.toJson(tokenVo);
    }

    public void refreshTokenDelete(@NotNull final User user){
        final String userEmail = user.getUserEmail();
        final String refreshToken = redisService.getValues(user.getUserEmail());

        if(!jwtValidation.isValidationRefreshTokenCheck(refreshToken)) throw new TokenException("재로그인이 필요합니다.");

        redisService.deleteValues(userEmail);
    }

    public void accessTokenBlack(@NotNull final User user, @NotNull final String authorization){
        if(jwtValidation.isValidationAuthorizationCheck(authorization)) throw new TokenException("엑세스 토큰이 존재하지 않거나 올바르지 않습니다.");
        final String prefixAuthorization = jwtProvider.replaceTokenPrefix(authorization);
        BLACK_LIST.put(user.getUserEmail(), prefixAuthorization);
    }
}
