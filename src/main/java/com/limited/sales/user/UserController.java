package com.limited.sales.user;

import com.limited.sales.annotation.CurrentUser;
import com.limited.sales.config.Constant;
import com.limited.sales.exception.sub.BadRequestException;
import com.limited.sales.user.vo.User;
import com.limited.sales.utils.HttpResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping(
    value = "/users",
    produces = MediaType.APPLICATION_JSON_VALUE,
    consumes = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Validated
public class UserController {
  private final UserService userService;

  @GetMapping("/email/duplicated")
  public HttpResponse<Void> checkEmailDuplication(
      @NotNull(message = "이메일이 존재하지 않습니다.") @Email(message = "이메일 형식이 아닙니다.") final String email) {

    if (!userService.checkEmailDuplication(email)) {
      return HttpResponse.toResponse(HttpStatus.ACCEPTED.value(), "가입 가능한 이메일 입니다.");
    } else {
      return HttpResponse.toResponse(HttpStatus.OK.value(), "이미 가입된 이메일 입니다.");
    }
  }

  @PostMapping
  public HttpResponse<Void> signUp(@RequestBody @Valid final User user) {
    userService.signUp(user);
    return HttpResponse.toResponse(HttpStatus.CREATED.value(), "계정이 정상적으로 생성되었습니다.");
  }

  @PatchMapping
  @Secured({Constant.ROLE_USER, Constant.ROLE_ADMIN})
  public HttpResponse<Void> updateUserInfo(
      @CurrentUser User currentUser,
      @RequestBody(required = false) @NotBlank(message = "전화번호가 존재하지 않습니다.")
          final String cellphone) {
    if (!StringUtils.isNumeric(cellphone)) {
      throw new BadRequestException("숫자만 입력 가능합니다.");
    }
    userService.updateUserInformation(currentUser, cellphone);
    return HttpResponse.toResponse(HttpStatus.OK.value(), "정보가 정상적으로 수정되었습니다.");
  }

  @PatchMapping("/password")
  @Secured({Constant.ROLE_USER, Constant.ROLE_ADMIN})
  public HttpResponse<Void> updateUserPassword(
      @CurrentUser final User currentUser,
      @RequestBody(required = false) @NotNull(message = "비밀번호가 존재하지 않습니다.")
          final Map<String, String> passwordData) {
    final String currentPassword = passwordData.get("currentPassword");
    final String newPassword = passwordData.get("newPassword");

    if (StringUtils.isBlank(currentPassword)) {
      throw new BadRequestException("현재 비밀번호가 존재하지 않습니다.");
    }

    if (!userService.checkPassword(currentUser, currentPassword)) {
      throw new BadRequestException("현재 비밀번호가 일치하지 않습니다.");
    }

    if (StringUtils.equals(newPassword, currentPassword)) {
      throw new BadRequestException("현재 비밀번호와 변경할 비밀번호가 동일합니다.");
    }

    userService.updatePassword(currentUser, newPassword);
    return HttpResponse.toResponse(HttpStatus.OK.value(), "비밀번호가 정상적으로 수정되었습니다.");
  }

  @DeleteMapping
  @Secured({Constant.ROLE_USER, Constant.ROLE_ADMIN})
  public HttpResponse<Void> deleteUser(@CurrentUser User currentUser) {
    userService.deleteUser(currentUser);
    return HttpResponse.toResponse(HttpStatus.OK.value(), "회원탈퇴가 정상적으로 이뤄졌습니다.");
  }

  // TODO :: 관리자가 사용자 강제 탈퇴 기능 구현 필요.
  @PatchMapping("/admin")
  @Secured({Constant.ROLE_USER, Constant.ROLE_ADMIN})
  public HttpResponse<Void> updateUserRoleToAdmin(
      @CurrentUser User currentUser,
      @RequestBody(required = false) @NotBlank(message = "코드가 존재하지 않습니다.") final String adminCode) {
    userService.updateUserRoleToAdmin(currentUser, adminCode);
    return HttpResponse.toResponse(HttpStatus.OK.value(), "관리자 권한을 부여 받았습니다.");
  }
}
