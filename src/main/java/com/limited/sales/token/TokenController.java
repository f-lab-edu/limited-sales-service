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

  /**
   * <h1>엑세스 토큰 재발급
   * <p>엑세스 토큰이 만료됐을 때 클라이언트에서 만료된 에러를 확인 후 <br>해당 reissue 메소드를 호출해 엑세스 토큰을 재발급 받는다.
   * @param user
   * @return ResponseEntity<String>
   */
  @PostMapping
  public ResponseEntity<String> reissue(@RequestBody final User user) {
    return ResponseEntity.ok().body(tokenService.reissue(user));
  }
}
