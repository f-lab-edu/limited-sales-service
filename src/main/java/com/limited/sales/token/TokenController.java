package com.limited.sales.token;

import com.limited.sales.user.vo.User;
import com.limited.sales.utils.JwtProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping(
        value = "/token",
        produces = MediaType.APPLICATION_JSON_VALUE,
        consumes = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class TokenController {
    private final TokenService tokenService;

    @PostMapping
    public ResponseEntity<String> reissue(@RequestBody final User user){
        return ResponseEntity.ok().body(tokenService.reissue(user));
    }

    @DeleteMapping
    public ResponseEntity<String> logout(@RequestBody final User user, final HttpServletRequest request){
        final String authorization = request.getHeader(JwtProperties.HEADER_STRING);
        tokenService.refreshTokenDelete(user);
        tokenService.accessTokenBlack(user, authorization);
        return ResponseEntity.ok().body("");
    }
}
