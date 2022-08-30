package com.limited.sales.user;

import com.limited.sales.annotation.CurrentUser;
import com.limited.sales.config.Constant;
import com.limited.sales.exception.sub.BadRequestException;
import com.limited.sales.user.vo.User;
import com.limited.sales.utils.HttpResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Map;

import static com.limited.sales.utils.MessageProperties.*;

@Slf4j
@Validated
@RestController
@RequestMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class UserController {
  private final UserService userService;
  private final MessageSourceAccessor message;

  @GetMapping("/email/duplicated")
  public HttpResponse<Void> checkEmailDuplication(
      @NotEmpty(message = "{" + EMAIL_NULL + "}") @Email(message = "{" + EMAIL_NONE_TYPE + "}")
          final String email) {
    if (userService.checkEmailDuplication(email)) {
      return HttpResponse.toResponse(HttpStatus.OK, message.getMessage(EMAIL_DUPLICATED_FAIL));
    } else {
      return HttpResponse.toResponse(
          HttpStatus.ACCEPTED, message.getMessage(EMAIL_DUPLICATED_SUCCESS));
    }
  }

  @PostMapping
  public HttpResponse<Void> signUp(@RequestBody @Valid final User user) {
    userService.signUp(user);
    return HttpResponse.toResponse(HttpStatus.CREATED, message.getMessage(SIGN_CREATE_SUCCESS));
  }

  @PatchMapping
  @Secured({Constant.ROLE_USER, Constant.ROLE_ADMIN})
  public HttpResponse<Void> updateUserInfo(
      @CurrentUser User currentUser,
      @RequestBody(required = false) @NotBlank(message = "{" + CELLPHONE_NULL + "}")
          final String cellphone) {
    if (!StringUtils.isNumeric(cellphone)) {
      throw new BadRequestException("숫자만 입력 가능합니다.");
    }
    userService.updateUserInformation(currentUser, cellphone);
    return HttpResponse.toResponse(HttpStatus.OK, message.getMessage(INFO_UPDATE_SUCCESS));
  }

  @PatchMapping("/password")
  @Secured({Constant.ROLE_USER, Constant.ROLE_ADMIN})
  public HttpResponse<Void> updateUserPassword(
      @CurrentUser final User currentUser,
      @RequestBody(required = false) @NotNull(message = "{" + PASSWORD_NULL + "}")
          final Map<String, String> passwordData) {
    final String currentPassword = passwordData.get("currentPassword");
    final String newPassword = passwordData.get("newPassword");

    if (StringUtils.isBlank(currentPassword)) {
      throw new BadRequestException(message.getMessage(CURRENT_PASSWORD_NULL));
    }

    if (StringUtils.isBlank(newPassword)) {
      throw new BadRequestException(message.getMessage(NEW_PASSWORD_NULL));
    }

    if (!userService.checkPassword(currentPassword, currentUser.getPassword())) {
      throw new BadRequestException(message.getMessage(CURRENT_PASSWORD_NOT_EQUALS));
    }

    if (StringUtils.equals(newPassword, currentPassword)) {
      throw new BadRequestException(message.getMessage(CURRENT_PASSWORD_NEW_PASSWORD_SAME));
    }

    userService.updatePassword(currentUser, newPassword);
    return HttpResponse.toResponse(HttpStatus.OK, message.getMessage(UPDATE_PASSWORD_SUCCESS));
  }

  @DeleteMapping
  @Secured({Constant.ROLE_USER, Constant.ROLE_ADMIN})
  public HttpResponse<Void> deleteUser(@CurrentUser User currentUser) {
    userService.deleteUser(currentUser);
    return HttpResponse.toResponse(HttpStatus.OK, message.getMessage(UNSIGNED_SUCCESS));
  }

  // TODO :: 관리자가 사용자 강제 탈퇴 기능 구현 필요.
  @PatchMapping("/admin")
  @Secured({Constant.ROLE_USER, Constant.ROLE_ADMIN})
  public HttpResponse<Void> updateUserRoleToAdmin(
      @CurrentUser User currentUser,
      @RequestBody(required = false) @NotBlank(message = "{" + ADMIN_CODE_NULL + "}")
          final String adminCode) {
    if (!Constant.ADMIN_CODE.equals(adminCode)) {
      throw new BadRequestException(message.getMessage(ADMIN_CODE_NOT_EQUALS));
    }

    userService.updateUserRoleToAdmin(currentUser, adminCode);
    return HttpResponse.toResponse(HttpStatus.OK, message.getMessage(ADMIN_AUTH_SUCCESS));
  }
}
