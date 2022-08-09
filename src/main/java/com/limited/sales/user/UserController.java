package com.limited.sales.user;

import com.limited.sales.annotation.CurrentUser;
import com.limited.sales.config.Constant;
import com.limited.sales.exception.sub.BadRequestException;
import com.limited.sales.exception.sub.DuplicatedIdException;
import com.limited.sales.user.vo.User;
import com.limited.sales.utils.HttpResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.util.Map;
import java.util.Optional;

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
  public ResponseEntity<String> checkEmailDuplication(@RequestBody final User parameterUser) {
    final User user = Optional.ofNullable(parameterUser).orElse(new User());

    if (!userService.checkEmailDuplication(user)) {
      return ResponseEntity.noContent().build();
    } else {
      throw new DuplicatedIdException("이미 가입된 이메일 입니다.");
    }
  }

  @PostMapping
  public ResponseEntity<String> signUp(@RequestBody @Validated final User user) {
    userService.signUp(user);
    return ResponseEntity.noContent().build();
  }

  @PatchMapping
  @Secured({Constant.ROLE_USER, Constant.ROLE_ADMIN})
  public ResponseEntity<String> updateUserInfo(
      @CurrentUser User user, @RequestBody final User targetUser) {
    userService.changeUserInformation(user, targetUser);
    HttpResponse.builder().code(200).build();
    return ResponseEntity.noContent().build();
  }

  @PatchMapping("/password")
  @Secured({Constant.ROLE_USER, Constant.ROLE_ADMIN})
  public ResponseEntity<String> changeUserPassword(
      @CurrentUser final User currentUser,
      @RequestBody final Map<String, String> changeData,
      @RequestHeader(Constant.CurrentPassword) @NotBlank(message = "현재 비밀번호를 입력하지 않았습니다.")
          final String currentPassword,
      Errors error) {
    if (error.hasErrors()) {
      throw new BadRequestException("비밀번호를 다시 입력해주세요.");
    }

    if (userService.checkPassword(currentUser, currentPassword)) {
      userService.changePassword(currentUser, changeData);
    } else {
      throw new BadRequestException("패스워드가 일치하지 않습니다.");
    }
    return ResponseEntity.noContent().build();
  }

  @DeleteMapping
  @Secured({Constant.ROLE_USER, Constant.ROLE_ADMIN})
  public ResponseEntity<String> deleteUser(@CurrentUser User user) {
    userService.deleteUser(user);
    return ResponseEntity.noContent().build();
  }

  @PatchMapping("/admin")
  @Secured({Constant.ROLE_USER, Constant.ROLE_ADMIN})
  public ResponseEntity<String> changeUserRoleToAdmin(
      @CurrentUser User user, @RequestHeader("AdminCode") final String adminCode) {
    userService.changeUserRoleToAdmin(adminCode, user);
    return ResponseEntity.noContent().build();
  }
}
