package com.limited.sales.user;

import com.limited.sales.error.exception.BadRequestException;
import com.limited.sales.error.exception.user.DuplicatedIdException;
import com.limited.sales.jwt.JwtProperties;
import com.limited.sales.user.vo.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {

  private static final String ADMIN_CODE = "ADMIN";

  private final UserService userService;

  /**
   * 회원가입
   *
   * @param user
   * @return
   */
  @PostMapping("/user")
  public ResponseEntity<String> join(@RequestBody @Validated User user, Errors errors) {
    // validation check
    if (errors.hasErrors())
      return new ResponseEntity<>("사용자 정보 입력을 다시 확인해주세요.", HttpStatus.BAD_REQUEST);
    userService.saveUser(user);
    return new ResponseEntity<>("회원가입이 완료되었습니다.", HttpStatus.CREATED);
  }

  /*
   * 회원가입 시 이메일의 중복체크를 진행한다. 중복체크는 회원가입 아이디 입력 후, 회원가입 요청시 두번 진행한다.
   * 아이디 중복체크를 한 후 회원가입 버튼을 누를 때 까지 동일한 아이디로 누군가 가입한다면
   * PK Error가 발생되고 실제로 회원가입이 진행되지 않을 수 있기 때문에
   * 회원가입을 눌렀을 때 한번 더 실행하는 것이 좋다.
   */

  /**
   * 이메일 중복 체크
   *
   * @param userEmail
   * @return
   */
  @GetMapping("/user/email")
  @ResponseStatus(HttpStatus.OK)
  public String userEmailChk(@RequestBody Map<String, String> userEmail) {
    if (userService.emailExists(String.valueOf(userEmail.get("userEmail")))) {
      throw new DuplicatedIdException("이미 존재하는 이메일 입니다.");
    } else {
      return "사용 가능한 이메일입니다.";
    }
  }

  /**
   * 회원정보 수정
   *
   * @param user
   * @return
   */
  @Secured({"ROLE_USER", "ROLE_ADMIN"})
  @ResponseStatus(HttpStatus.OK)
  @PatchMapping("/user")
  public String userInfoChange(@CurrentUser User user, @RequestBody User reqUser) {
    if (!(reqUser.getUserEmail()).equals(user.getUserEmail()))
      throw new BadRequestException("사용자 이메일 정보를 다시 확인해주세요.");
    User changeUser =
        user.builder()
            .userEmail(reqUser.getUserEmail())
            .userCellphone(reqUser.getUserCellphone())
            .build();
    userService.updateUser(changeUser);
    return "사용자 정보가 변경되었습니다.";
  }

  /**
   * 회원 탈퇴
   *
   * @param user
   * @return
   */
  @Secured({"ROLE_USER", "ROLE_ADMIN"})
  @ResponseStatus(HttpStatus.OK)
  @DeleteMapping("/user")
  public String userInfoDelete(@CurrentUser User user, @RequestBody User reqUser) {
    if (userService.checkPassword(reqUser)) {
      userService.deleteUser(user);
      return "회원탈퇴가 완료되었습니다.";
    } else {
      throw new BadRequestException("비밀번호 정보를 다시 확인해주세요.");
    }
  }

  /**
   * 관리자 계정 전환
   *
   * @param user
   * @param adminCode
   * @return
   */
  @Secured({"ROLE_USER", "ROLE_ADMIN"})
  @ResponseStatus(HttpStatus.OK)
  @PatchMapping("/user/admin")
  public String userChangeAdmin(
      @CurrentUser User user, @RequestHeader("adminCode") String adminCode) {
    if (adminCode.equals(ADMIN_CODE)) {
      userService.updateUserRole(user.getUserEmail());
      return "관리자 계정으로 전환이 완료되었습니다.";
    } else {
      throw new BadRequestException("잘못된 인증코드입니다.");
    }
  }

  /**
   * accessToken 재발급 - 로그인 연장
   *
   * @param user
   * @return
   */
  @GetMapping("/re-issue")
  @ResponseStatus(HttpStatus.OK)
  public String reIssue(@CurrentUser User user, HttpServletResponse response) {
    String userEmail = user.getUserEmail();
    String accessToken = userService.reIssueAccessToken(user, userEmail);
    // accessToken을 발급해준다. response에 Authorization으로 실어준다.
    response.addHeader(
        JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX + accessToken); // 한칸 반드시 띄워줘야함.
    return "로그인 연장 완료";
  }

  /**
   * 로그아웃
   *
   * @param authorization
   * @param request
   * @return
   */
  @GetMapping("/logout")
  @ResponseStatus(HttpStatus.OK)
  public String logout(
      @CurrentUser User user,
      @RequestHeader(JwtProperties.HEADER_STRING) String authorization,
      HttpServletRequest request) {
    userService.logout(user.getUserEmail(), authorization);
    return "로그아웃 성공";
  }
}
