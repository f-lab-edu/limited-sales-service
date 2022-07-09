package com.limited.sales.token;

import com.google.gson.Gson;
import com.limited.sales.exception.sub.TokenException;
import com.limited.sales.redis.RedisService;
import com.limited.sales.user.vo.User;
import com.limited.sales.utils.JwtProperties;
import com.limited.sales.utils.JwtProvider;
import com.limited.sales.utils.JwtValidation;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(
        value = "/token",
        produces = MediaType.APPLICATION_JSON_VALUE,
        consumes = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class TokenController {
    private final JwtProvider jwtProvider = new JwtProvider();
    private final JwtValidation jwtValidation = new JwtValidation();
    private final RedisService redisService;

    @PostMapping
    public ResponseEntity<String> accessReissue(@RequestBody final User user){
        final String refreshToken = redisService.getValues(user.getUserEmail());
        final Gson gson = new Gson();

        if(!jwtValidation.isValidationRefreshTokenCheck(refreshToken)) throw new TokenException("리프레쉬 토큰이 정상적이지 않습니다.");
        final String token = JwtProperties.TOKEN_PREFIX + jwtProvider.createAccessTokenMethod(user);

        return ResponseEntity
                .ok()
                .body(gson.toJson(Token.builder()
                                        .accessToken(token)
                                        .build())
                );
    }

    @Builder
    @Getter
    private static final class Token {
        private String accessToken;
        private String refreshToken;
    }
}
