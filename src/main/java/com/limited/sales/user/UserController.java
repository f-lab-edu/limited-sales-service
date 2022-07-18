package com.limited.sales.user;

import com.limited.sales.config.Constant;
import com.limited.sales.exception.sub.BadRequestException;
import com.limited.sales.principal.PrincipalDetails;
import com.limited.sales.user.vo.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

@Slf4j
@RestController
@RequestMapping(
    value = "/user",
    produces = MediaType.APPLICATION_JSON_VALUE,
    consumes = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class UserController {
  private final UserService userService;

  @GetMapping("/email")
  public ResponseEntity<String> checkEmailDuplication(@RequestBody final User user) {
    userService.checkEmailDuplication(user);
    return ResponseEntity.noContent().build();
  }

  @PostMapping
  public ResponseEntity<String> signUp(@RequestBody final User user) {
    userService.signUp(user);
    return ResponseEntity.noContent().build();
  }

  @GetMapping
  @Secured({"ROLE_USER, ROLE_ADMIN"})
  public ResponseEntity<String> findUser(@RequestBody final User user) {
    final String findUserEmail = user.getUserEmail();
    final User byUser = userService.findByEmail(findUserEmail);

    final String toJson = Constant.getGson().toJson(byUser, User.class);
    return ResponseEntity.ok(toJson);
  }

  @PatchMapping
  @Secured({"ROLE_USER, ROLE_ADMIN"})
  public ResponseEntity<String> updateUserInfo(
      final Authentication authentication, @RequestBody final User targetUser) {
    final User user = getUser(authentication);
    userService.changeUserInformation(user, targetUser);
    return ResponseEntity.noContent().build();
  }

  @PatchMapping("/password")
  @Secured({"ROLE_USER, ROLE_ADMIN"})
  public ResponseEntity<String> changeUserPassword(
      @RequestBody final User user,
      @RequestHeader("OldPassword") @Validated final String oldPassword,
      Errors error) {
        if (error.hasErrors()) {
          throw new BadRequestException("비밀번호를 다시 입력해주세요.");
    }

    if (userService.checkPassword(oldPassword)) {
      userService.changePassword(user);
    } else {
      throw new RuntimeException("패스워드가 일치하지 않습니다.");
    }
    return ResponseEntity.noContent().build();
  }

  @DeleteMapping
  @Secured({"ROLE_USER, ROLE_ADMIN"})
  public ResponseEntity<String> deleteUser(final Authentication authentication) {
    final User user = getUser(authentication);
    userService.deleteUser(user);
    return ResponseEntity.noContent().build();
  }

  @PatchMapping("/admin")
  @Secured({"ROLE_USER, ROLE_ADMIN"})
  public ResponseEntity<String> changeUserRoleToAdmin(
      final Authentication authentication, @RequestHeader("AdminCode") final String adminCode) {
    final User user = getUser(authentication);
    userService.changeUserRoleToAdmin(adminCode, user);
    return ResponseEntity.noContent().build();
  }

  private User getUser(@NotNull final Authentication authentication) {
    final PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
    return principal.getUser();
  }
}
