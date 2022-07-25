package com.limited.sales;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/test")
public class TestAuthenticationController {

  @Secured({"ROLE_USER", "ROLE_ADMIN"})
  @RequestMapping("/both")
  public void authentication() {
    log.debug(
        "TestAuthenticationController.authentication :::: ROLE_USER, ROLE_ADMIN :::: 사용자, 관리자 권한 체크 성공");
  }

  @Secured({"ROLE_USER"})
  @RequestMapping("/user")
  public void userAuthentication() {
    log.debug("TestAuthenticationController.userAuthentication :::: ROLE_USER :::: 사용자 권한 체크 성공");
  }

  @Secured({"ROLE_ADMIN"})
  @RequestMapping("/admin")
  public void adminAuthentication() {
    log.debug("TestAuthenticationController.adminAuthentication :::: ROLE_ADMIN :::: 관리자 권한 체크 성공");
  }

  @RequestMapping("/none")
  public void noneAuthentication() {
    log.debug("TestAuthenticationController.notAuthentication :::: NONE :::: 권한 없음 체크 성공");
  }
}
