package com.limited.sales.user;

import com.google.gson.Gson;
import com.limited.sales.config.Constant;
import com.limited.sales.config.LazyHolderObject;
import com.limited.sales.user.vo.User;
import com.limited.sales.utils.JwtProperties;
import com.limited.sales.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(value = "classpath:/application.properties")
@Transactional
@Slf4j
class UserControllerTest {

  private final Gson gson = LazyHolderObject.getGson();
  @Autowired private MockMvc mockMvc;

  /**
   *
   *
   * <h1>이메일 중복 확인 기능</h1>
   */
  @Test
  @DisplayName("UserController.이메일 중복체크 - 등록되지 않은 이메일")
  void checkEmailDuplicationNo() throws Exception {
    User user = User.builder().email("cometo@naver.com").build();

    mockMvc
        .perform(
            get("/user/email").content(gson.toJson(user)).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is2xxSuccessful())
        .andDo(print());
  }

  @Test
  @DisplayName("UserController.이메일 중복체크 - 이미 등록된 이메일 체크")
  void checkEmailDuplicationYes() throws Exception {
    User user = User.builder().email("test@test").build();

    mockMvc
        .perform(
            get("/user/email").content(gson.toJson(user)).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is4xxClientError())
        .andDo(print());
  }

  @Test
  @DisplayName("UserController.이메일 중복체크 - 이메일 값 empty")
  void checkEmailDuplicationEmailEmpty() throws Exception {
    User user = User.builder().email("").build();

    mockMvc
        .perform(
            get("/user/email").content(gson.toJson(user)).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is4xxClientError())
        .andDo(print());
  }

  @Test
  @DisplayName("UserController.이메일 중복체크 - 이메일 값 null")
  void checkEmailDuplicationEmailNull() throws Exception {
    User user = User.builder().email(null).build();

    mockMvc
        .perform(
            get("/user/email").content(gson.toJson(user)).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is4xxClientError())
        .andDo(print());
  }

  @Test
  @DisplayName("UserController.이메일 중복체크 - body empty")
  void checkEmailDuplicationBodyEmpty() throws Exception {
    mockMvc
        .perform(get("/user/email").content("").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is4xxClientError())
        .andDo(print());
  }

  /**
   *
   *
   * <h1>회원가입 기능</h1>
   */
  @Test
  @Rollback
  @DisplayName("UserController.회원가입 - 정상")
  void signUp() throws Exception {
    User user =
        User.builder().email("ohjeung@gamil.com").password("1234").cellphone("01011112222").build();

    mockMvc
        .perform(post("/user").content(gson.toJson(user)).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is2xxSuccessful())
        .andDo(print());
  }

  @Test
  @Rollback
  @DisplayName("UserController.회원가입 - 이메일 누락")
  void signUpNoEmail() throws Exception {
    User user = User.builder().password("1234").cellphone("01011112222").build();

    mockMvc
        .perform(post("/user").content(gson.toJson(user)).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is4xxClientError())
        .andDo(print());
  }

  @Test
  @Rollback
  @DisplayName("UserController.회원가입 - 비밀번호 누락")
  void signUpNoPassword() throws Exception {
    User user = User.builder().email("ohjeung@gamil.com").cellphone("01011112222").build();

    mockMvc
        .perform(post("/user").content(gson.toJson(user)).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is4xxClientError())
        .andDo(print());
  }

  @Test
  @Rollback
  @DisplayName("UserController.회원가입 - 이메일 공백")
  void signUpEmptyEmail() throws Exception {
    User user = User.builder().email("").password("1234").cellphone("01011112222").build();
    mockMvc
        .perform(post("/user").content(gson.toJson(user)).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is4xxClientError())
        .andDo(print());
  }

  @Test
  @Rollback
  @DisplayName("UserController.회원가입 - 비밀번호 공백")
  void signUpEmptyPassword() throws Exception {
    User user =
        User.builder().email("ohjeung@gamil.com").password("").cellphone("01011112222").build();
    mockMvc
        .perform(post("/user").content(gson.toJson(user)).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is4xxClientError())
        .andDo(print());
  }

  /**
   *
   *
   * <h1>사용자 정보 수정 기능</h1>
   */
  @Test
  @Rollback
  @DisplayName("UserController.사용자 정보 수정 - 정보 수정 성공")
  void updateUserInfo() throws Exception {
    User user = User.builder().email("test@test").password("1234").cellphone("01022223333").build();

    String prefixToken = JwtProperties.TOKEN_PREFIX + JwtUtils.createAccessToken(user);

    mockMvc
        .perform(
            patch("/user")
                .header(JwtProperties.HEADER_STRING, prefixToken)
                .content(gson.toJson(user))
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is2xxSuccessful())
        .andDo(print());
  }

  @Test
  @Rollback
  @DisplayName("UserController.사용자 정보 수정 - 엑세스 토큰 없음")
  void updateUserNoToken() throws Exception {
    User user = User.builder().email("test@test").password("1234").cellphone("01022223333").build();

    mockMvc
        .perform(patch("/user").content(gson.toJson(user)).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is4xxClientError())
        .andDo(print());
  }

  @Test
  @Rollback
  @DisplayName("UserController.사용자 정보 수정 - 중복된 사용자 정보 다시 수정하기")
  void updateUserDuplicationInfo() throws Exception {
    User user = User.builder().email("test@test").password("1234").cellphone("01011112222").build();

    String prefixToken = JwtProperties.TOKEN_PREFIX + JwtUtils.createAccessToken(user);

    mockMvc
        .perform(
            patch("/user")
                .header(JwtProperties.HEADER_STRING, prefixToken)
                .content(gson.toJson(user))
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is2xxSuccessful())
        .andDo(print());
  }

  @Test
  @Rollback
  @DisplayName("UserController.사용자 정보 수정 - 전화번호 데이터 NULL")
  void updateUserNullCellphone() throws Exception {
    User user = User.builder().email("test@test").password("1234").build();

    String prefixToken = JwtProperties.TOKEN_PREFIX + JwtUtils.createAccessToken(user);

    mockMvc
        .perform(
            patch("/user")
                .header(JwtProperties.HEADER_STRING, prefixToken)
                .content(gson.toJson(user))
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is4xxClientError())
        .andDo(print());
  }

  @Test
  @Rollback
  @DisplayName("UserController.사용자 정보 수정 - 전화번호 데이터 empty")
  void updateUserEmptyCellphone() throws Exception {
    User user = User.builder().cellphone("").email("test@test").password("1234").build();

    String prefixToken = JwtProperties.TOKEN_PREFIX + JwtUtils.createAccessToken(user);

    mockMvc
        .perform(
            patch("/user")
                .header(JwtProperties.HEADER_STRING, prefixToken)
                .content(gson.toJson(user))
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is4xxClientError())
        .andDo(print());
  }

  /**
   *
   *
   * <h1>사용자 비밀번호 변경 기능</h1>
   */
  @Test
  @Rollback
  @DisplayName("UserController.비밀번호 변경 - 정상적으로 패스워드 변경")
  void changeUserPassword() throws Exception {
    User user = User.builder().email("test@test").password("1234").build();
    Map<String, String> userData = new HashMap<>();
    String prefixToken = JwtProperties.TOKEN_PREFIX + JwtUtils.createAccessToken(user);
    String currentPassword = "1234";

    userData.put("newPassword", "5678");

    mockMvc
        .perform(
            patch("/user/password")
                .header("current_password", currentPassword)
                .header(JwtProperties.HEADER_STRING, prefixToken)
                .content(gson.toJson(userData))
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is2xxSuccessful())
        .andDo(print());
  }

  @Test
  @Rollback
  @DisplayName("UserController.비밀번호 변경 - 현재 패스워드가 일치하지 않음")
  void changeUserPasswordFail() throws Exception {
    User user = User.builder().email("test@test").password("1234").build();
    Map<String, String> userData = new HashMap<>();
    String prefixToken = JwtProperties.TOKEN_PREFIX + JwtUtils.createAccessToken(user);
    String currentPassword = "2929";

    userData.put("newPassword", "5678");

    mockMvc
        .perform(
            patch("/user/password")
                .header("current_password", currentPassword)
                .header(JwtProperties.HEADER_STRING, prefixToken)
                .content(gson.toJson(userData))
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is4xxClientError())
        .andDo(print());
  }

  @Test
  @Rollback
  @DisplayName("UserController.비밀번호 변경 - 현재 패스워드 입력하지 않음")
  void changeUserCurrentPasswordEmpty() throws Exception {
    User user = User.builder().email("test@test").password("1234").build();
    Map<String, String> userData = new HashMap<>();
    String prefixToken = JwtProperties.TOKEN_PREFIX + JwtUtils.createAccessToken(user);
    String currentPassword = "";

    userData.put("newPassword", "5678");

    mockMvc
        .perform(
            patch("/user/password")
                .header("current_password", currentPassword)
                .header(JwtProperties.HEADER_STRING, prefixToken)
                .content(gson.toJson(userData))
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is4xxClientError())
        .andDo(print());
  }

  @Test
  @Rollback
  @DisplayName("UserController.비밀번호 변경 - 새로운 비밀번호 empty")
  void changeUserNewPasswordEmpty() throws Exception {
    User user = User.builder().email("test@test").password("1234").build();
    Map<String, String> userData = new HashMap<>();
    String prefixToken = JwtProperties.TOKEN_PREFIX + JwtUtils.createAccessToken(user);
    String currentPassword = "1234";

    userData.put("newPassword", "");

    mockMvc
        .perform(
            patch("/user/password")
                .header("current_password", currentPassword)
                .header(JwtProperties.HEADER_STRING, prefixToken)
                .content(gson.toJson(userData))
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is4xxClientError())
        .andDo(print());
  }

  @Test
  @Rollback
  @DisplayName("UserController.비밀번호 변경 - 새로운 비밀번호 null")
  void changeUserNewPasswordNull() throws Exception {
    User user = User.builder().email("test@test").password("1234").build();
    Map<String, String> userData = new HashMap<>();
    String prefixToken = JwtProperties.TOKEN_PREFIX + JwtUtils.createAccessToken(user);
    String currentPassword = "1234";

    userData.put("newPassword", null);

    mockMvc
        .perform(
            patch("/user/password")
                .header("current_password", currentPassword)
                .header(JwtProperties.HEADER_STRING, prefixToken)
                .content(gson.toJson(userData))
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is4xxClientError())
        .andDo(print());
  }

  @Test
  @Rollback
  @DisplayName("UserController.비밀번호 변경 - 현재 비밀번호 null")
  void changeUserCurrentPasswordNull() throws Exception {
    User user = User.builder().email("test@test").password("1234").build();
    Map<String, String> userData = new HashMap<>();
    String prefixToken = JwtProperties.TOKEN_PREFIX + JwtUtils.createAccessToken(user);
    String currentPassword = "1234";

    userData.put("newPassword", null);

    mockMvc
        .perform(
            patch("/user/password")
                .header(JwtProperties.HEADER_STRING, prefixToken)
                .content(gson.toJson(userData))
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is4xxClientError())
        .andDo(print());
  }

  /**
   *
   *
   * <h1>사용자 삭제 기능</h1>
   */
  @Test
  @Rollback
  @DisplayName("UserController.회원탈퇴 - 탈퇴")
  void deleteUser() throws Exception {
    User user = User.builder().email("test@test").password("1234").build();
    String prefixToken = JwtProperties.TOKEN_PREFIX + JwtUtils.createAccessToken(user);

    mockMvc
        .perform(
            delete("/user")
                .header(JwtProperties.HEADER_STRING, prefixToken)
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is2xxSuccessful())
        .andDo(print());
  }

  /**
   *
   *
   * <h1>사용자 권한 변경 기능</h1>
   */
  @Test
  @Rollback
  @DisplayName("UserController.관리자 - 관리자 권한 부여")
  void changeUserRoleToAdmin() throws Exception {
    User user = User.builder().email("test@test").password("1234").build();
    String prefixToken = JwtProperties.TOKEN_PREFIX + JwtUtils.createAccessToken(user);

    mockMvc
        .perform(
            patch("/user/admin")
                .header("AdminCode", Constant.ADMIN_CODE)
                .header(JwtProperties.HEADER_STRING, prefixToken)
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is2xxSuccessful())
        .andDo(print());
  }

  @Test
  @Rollback
  @DisplayName("UserController.관리자 - 관리자 코드 공백")
  void changeUserRoleEmptyAdminCode() throws Exception {
    User user = User.builder().email("test@test").password("1234").build();
    String prefixToken = JwtProperties.TOKEN_PREFIX + JwtUtils.createAccessToken(user);

    mockMvc
        .perform(
            patch("/user/admin")
                .header("AdminCode", "")
                .header(JwtProperties.HEADER_STRING, prefixToken)
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is4xxClientError())
        .andDo(print());
  }
}
