package com.limited.sales.token;

import com.limited.sales.annotation.CurrentUser;
import com.limited.sales.user.vo.User;
import com.limited.sales.utils.HttpResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/token", produces = MediaType.APPLICATION_JSON_VALUE)
public class TokenController {
  private final TokenService tokenService;

  /**
   *
   *
   * <h1>엑세스 토큰 재발급
   *
   * <p>엑세스 토큰이 만료됐을 때 클라이언트에서 만료된 에러를 확인 후 <br>
   * 해당 reissue 메소드를 호출해 엑세스 토큰을 재발급 받는다.
   *
   * @param user
   * @return ResponseEntity<String>
   */
  @PostMapping
  public HttpResponse<TokenVo> reissueAccessToken(@CurrentUser User user) {
    return HttpResponse.toResponse(HttpStatus.OK, "토큰이 재발급되었습니다.", tokenService.reissue(user));
  }
}
