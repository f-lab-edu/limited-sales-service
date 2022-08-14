package com.limited.sales.user;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.limited.sales.config.Constant;
import com.limited.sales.config.GsonSingleton;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(value = "classpath:/application.properties")
@Transactional
@Slf4j
class UserControllerTest {

  private final Gson gson = GsonSingleton.getGson();
  @Autowired private MockMvc mockMvc;
  @Autowired private UserService userService;

  /**
   *
   *
   * <h1>이메일 중복 확인 기능</h1>
   */
  @Test
  @DisplayName("UserController.이메일 중복체크 - 등록되지 않은 이메일")
  void checkEmailDuplicationNo() throws Exception {
    mockMvc
        .perform(
            get("/users/email/duplicated")
                .param("email", "cometo@naver.com")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is2xxSuccessful())
        .andExpect(jsonPath("$.code").value(202))
        .andExpect(jsonPath("$.message").value("가입 가능한 이메일 입니다."))
        .andDo(print());
  }

  @Test
  @DisplayName("UserController.이메일 중복체크 - 이미 등록된 이메일 체크")
  void checkEmailDuplicationYes() throws Exception {
    mockMvc
        .perform(
            get("/users/email/duplicated")
                .param("email", "test@test")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is2xxSuccessful())
        .andExpect(jsonPath("$.code").value(200))
        .andExpect(jsonPath("$.message").value("이미 가입된 이메일 입니다."))
        .andDo(print());
  }

  @Test
  @DisplayName("UserController.이메일 중복체크 - 이메일 값 empty")
  void checkEmailDuplicationEmailEmpty() throws Exception {

    mockMvc
        .perform(
            get("/users/email/duplicated")
                .param("email", "")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is4xxClientError())
        .andExpect(jsonPath("$.message").value("이메일이 존재하지 않습니다."))
        .andExpect(jsonPath("$.code").value(400))
        .andDo(print());
  }

  @Test
  @DisplayName("UserController.이메일 중복체크 - 이메일 값 null")
  void checkEmailDuplicationEmailNull() throws Exception {
    mockMvc
        .perform(
            get("/users/email/duplicated")
                .param("email", " ")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is4xxClientError())
        .andExpect(jsonPath("$.message").value("이메일 형식이 아닙니다."))
        .andExpect(jsonPath("$.code").value(400))
        .andDo(print());
  }

  @Test
  @DisplayName("UserController.이메일 중복체크 - body empty")
  void checkEmailDuplicationBodyEmpty() throws Exception {
    mockMvc
        .perform(get("/users/email/duplicated").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is4xxClientError())
        .andExpect(jsonPath("$.message").value("이메일이 존재하지 않습니다."))
        .andExpect(jsonPath("$.code").value(400))
        .andDo(print());
  }

  @Test
  @DisplayName("UserController.이메일 중복체크 - 이메일 형식이 아님")
  void checkEmailDuplication_no_email_type() throws Exception {
    mockMvc
        .perform(
            get("/users/email/duplicated")
                .param("email", "asd")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is4xxClientError())
        .andExpect(jsonPath("$.message").value("이메일 형식이 아닙니다."))
        .andExpect(jsonPath("$.code").value(400))
        .andDo(print());
  }

  @Test
  @DisplayName("UserController.이메일 중복체크 - 이메일 형식이 아님(골뱅이 만)")
  void checkEmailDuplication_no_email_type2() throws Exception {
    mockMvc
        .perform(
            get("/users/email/duplicated")
                .param("email", "asd@")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is4xxClientError())
        .andExpect(jsonPath("$.message").value("이메일 형식이 아닙니다."))
        .andExpect(jsonPath("$.code").value(400))
        .andDo(print());
  }

  @Test
  @DisplayName("UserController.이메일 중복체크 - 이메일 형식이 아님(골뱅이 이후 공백)")
  void checkEmailDuplication_no_email_type3() throws Exception {
    mockMvc
        .perform(
            get("/users/email/duplicated")
                .param("email", "asd@ ")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is4xxClientError())
        .andExpect(jsonPath("$.message").value("이메일 형식이 아닙니다."))
        .andExpect(jsonPath("$.code").value(400))
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
        .perform(post("/users").content(gson.toJson(user)).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is2xxSuccessful())
        .andExpect(jsonPath("$.code").value(201))
        .andExpect(jsonPath("$.message").value("계정이 정상적으로 생성되었습니다."))
        .andDo(print());
  }

  @Test
  @Rollback
  @DisplayName("UserController.회원가입 - 이메일 누락")
  void signUpNoEmail() throws Exception {
    User user = User.builder().password("1234").cellphone("01011112222").build();

    mockMvc
        .perform(post("/users").content(gson.toJson(user)).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is4xxClientError())
        .andExpect(jsonPath("$.code").value(400))
        .andExpect(jsonPath("$.message").value("이메일을 입력하지 않았습니다."))
        .andDo(print());
  }

  @Test
  @Rollback
  @DisplayName("UserController.회원가입 - 비밀번호 누락")
  void signUpNoPassword() throws Exception {
    User user = User.builder().email("ohjeung@gamil.com").cellphone("01011112222").build();

    mockMvc
        .perform(post("/users").content(gson.toJson(user)).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is4xxClientError())
        .andExpect(jsonPath("$.code").value(400))
        .andExpect(jsonPath("$.message").value("비밀번호를 입력하지 않았습니다."))
        .andDo(print());
  }

  @Test
  @Rollback
  @DisplayName("UserController.회원가입 - 이메일 공백")
  void signUpEmptyEmail() throws Exception {
    User user = User.builder().email("").password("1234").cellphone("01011112222").build();
    mockMvc
        .perform(post("/users").content(gson.toJson(user)).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is4xxClientError())
        .andExpect(jsonPath("$.code").value(400))
        .andExpect(jsonPath("$.message").value("이메일을 입력하지 않았습니다."))
        .andDo(print());
  }

  @Test
  @Rollback
  @DisplayName("UserController.회원가입 - 비밀번호 공백")
  void signUpEmptyPassword() throws Exception {
    User user =
        User.builder().email("ohjeung@gamil.com").password("").cellphone("01011112222").build();
    mockMvc
        .perform(post("/users").content(gson.toJson(user)).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is4xxClientError())
        .andExpect(jsonPath("$.code").value(400))
        .andExpect(jsonPath("$.message").value("비밀번호를 입력하지 않았습니다."))
        .andDo(print());
  }

  @Test
  @Rollback
  @DisplayName("UserController.회원가입 - 중복 회원가입")
  void signUp_duplicated() throws Exception {
    User user =
        User.builder().email("ohjeung@naver.com").password("1234").cellphone("01011112222").build();

    mockMvc
        .perform(post("/users").content(gson.toJson(user)).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is4xxClientError())
        .andExpect(jsonPath("$.code").value(400))
        .andExpect(jsonPath("$.message").value("이미 존재하는 계정입니다."))
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
    User user = User.builder().email("test@test").password("1234").cellphone("01011112222").build();
    String prefixToken = JwtProperties.TOKEN_PREFIX + JwtUtils.createAccessToken(user);
    Map<String, String> data = new HashMap<>();
    data.put("cellphone", "01011113333");

    mockMvc
        .perform(
            patch("/users")
                .header(JwtProperties.HEADER_STRING, prefixToken)
                .content("01011113333")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is2xxSuccessful())
        .andDo(print());

    User foundUser = userService.findByEmail(user.getEmail());
    assertThat(foundUser.getCellphone()).isNotNull().isEqualTo("01011113333");
  }

  @Test
  @Rollback
  @DisplayName("UserController.사용자 정보 수정 - 엑세스 토큰 없음")
  void updateUserNoToken() throws Exception {

    mockMvc
        .perform(patch("/users").content("01022223333").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is4xxClientError())
        .andDo(print());
  }

  @Test
  @Rollback
  @DisplayName("UserController.사용자 정보 수정 - 전화번호 empty")
  void updateUserNullCellphone() throws Exception {
    User user = User.builder().email("test@test").password("1234").build();
    String prefixToken = JwtProperties.TOKEN_PREFIX + JwtUtils.createAccessToken(user);

    mockMvc
        .perform(
            patch("/users")
                .header(JwtProperties.HEADER_STRING, prefixToken)
                .content("")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is4xxClientError())
        .andDo(print());
  }

  @Test
  @Rollback
  @DisplayName("UserController.사용자 정보 수정 - 전화번호 문자열")
  void updateUserEmptyCellphone() throws Exception {
    User user = User.builder().cellphone("").email("test@test").password("1234").build();
    String prefixToken = JwtProperties.TOKEN_PREFIX + JwtUtils.createAccessToken(user);

    mockMvc
        .perform(
            patch("/users")
                .header(JwtProperties.HEADER_STRING, prefixToken)
                .content("string-data")
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
  @DisplayName("UserController.비밀번호 변경 - 정상적으로 비밀번호 변경")
  void changeUserPassword() throws Exception {
    User user = User.builder().email("test@test").password("1234").build();
    String prefixToken = JwtProperties.TOKEN_PREFIX + JwtUtils.createAccessToken(user);

    JsonObject obj = new JsonObject();
    obj.addProperty("currentPassword", "1234");
    obj.addProperty("newPassword", "2222");

    mockMvc
        .perform(
            patch("/users/password")
                .header(JwtProperties.HEADER_STRING, prefixToken)
                .content(obj.toString())
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is2xxSuccessful())
        .andExpect(jsonPath("$.code").value(200))
        .andExpect(jsonPath("$.message").value("비밀번호가 정상적으로 수정되었습니다."))
        .andDo(print());
  }

  @Test
  @Rollback
  @DisplayName("UserController.비밀번호 변경 - 현재 비밀번호가 일치하지 않음")
  void changeUserPasswordFail() throws Exception {
    User user = User.builder().email("test@test").password("1234").build();
    String prefixToken = JwtProperties.TOKEN_PREFIX + JwtUtils.createAccessToken(user);

    JsonObject obj = new JsonObject();
    obj.addProperty("currentPassword", "2323");
    obj.addProperty("newPassword", "4444");

    mockMvc
        .perform(
            patch("/users/password")
                .header(JwtProperties.HEADER_STRING, prefixToken)
                .content(obj.toString())
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is4xxClientError())
        .andExpect(jsonPath("$.code").value(400))
        .andExpect(jsonPath("$.message").value("현재 비밀번호가 일치하지 않습니다."))
        .andDo(print());
  }

  @Test
  @Rollback
  @DisplayName("UserController.비밀번호 변경 - 현재 비밀번호 empty")
  void changeUserCurrentPasswordEmpty() throws Exception {
    User user = User.builder().email("test@test").password("1234").build();
    String prefixToken = JwtProperties.TOKEN_PREFIX + JwtUtils.createAccessToken(user);

    JsonObject obj = new JsonObject();
    obj.addProperty("currentPassword", "");
    obj.addProperty("newPassword", "4444");

  @Test
  @Rollback
  @DisplayName("UserController.비밀번호 변경 - 현재 비밀번호 null")
  void changeUserCurrentPasswordNull() throws Exception {
    User user = User.builder().email("test@test").password("1234").build();
    String prefixToken = JwtProperties.TOKEN_PREFIX + JwtUtils.createAccessToken(user);

    User updatePasswordUser = User.builder().password("4444").build();
    mockMvc
        .perform(
            patch("/users/password")
                .header(JwtProperties.HEADER_STRING, prefixToken)
                .content(obj.toString())
                .param("currentPassword", "")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is4xxClientError())
        .andExpect(jsonPath("$.code").value(400))
        .andExpect(jsonPath("$.message").value("현재 비밀번호가 존재하지 않습니다."))
        .andDo(print());
  }

  @Test
  @Rollback
  @DisplayName("UserController.비밀번호 변경 - 현재 비밀번호 null")
  void changeUserCurrentPasswordNull() throws Exception {
    User user = User.builder().email("test@test").password("1234").build();
    String prefixToken = JwtProperties.TOKEN_PREFIX + JwtUtils.createAccessToken(user);

    JsonObject obj = new JsonObject();
    obj.addProperty("newPassword", "4444");

    mockMvc
        .perform(
            patch("/users/password")
                .header(JwtProperties.HEADER_STRING, prefixToken)
                .content(obj.toString())
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is4xxClientError())
        .andExpect(jsonPath("$.code").value(400))
        .andExpect(jsonPath("$.message").value("현재 비밀번호가 존재하지 않습니다."))
        .andDo(print());
  }

  @Test
  @Rollback
  @DisplayName("UserController.비밀번호 변경 - 새로운 비밀번호 empty")
  void changeUserNewPasswordEmpty() throws Exception {
    User user = User.builder().email("test@test").password("1234").build();
    String prefixToken = JwtProperties.TOKEN_PREFIX + JwtUtils.createAccessToken(user);

    JsonObject obj = new JsonObject();
    obj.addProperty("currentPassword", "1234");
    obj.addProperty("newPassword", "");

    mockMvc
        .perform(
            patch("/users/password")
                .header(JwtProperties.HEADER_STRING, prefixToken)
                .content(obj.toString())
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is4xxClientError())
        .andExpect(jsonPath("$.code").value(400))
        .andExpect(jsonPath("$.message").value("변경할 비밀번호가 존재하지 않습니다."))
        .andDo(print());
  }

  @Test
  @Rollback
  @DisplayName("UserController.비밀번호 변경 - 새로운 비밀번호 null")
  void changeUserNewPasswordNull() throws Exception {
    User user = User.builder().email("test@test").password("1234").build();
    String prefixToken = JwtProperties.TOKEN_PREFIX + JwtUtils.createAccessToken(user);

    JsonObject obj = new JsonObject();
    obj.addProperty("currentPassword", "1234");

    mockMvc
        .perform(
            patch("/users/password")
                .header(JwtProperties.HEADER_STRING, prefixToken)
                .content(obj.toString())
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is4xxClientError())
        .andExpect(jsonPath("$.code").value(400))
        .andExpect(jsonPath("$.message").value("변경할 비밀번호가 존재하지 않습니다."))
        .andDo(print());
  }

  @Test
  @Rollback
  @DisplayName("UserController.비밀번호 변경 - 새로운 비밀번호, 현재 비밀번호 같을 경우")
  void changeUserNewPassword_current_password_is_same() throws Exception {
    User user = User.builder().email("test@test").password("1234").build();
    String prefixToken = JwtProperties.TOKEN_PREFIX + JwtUtils.createAccessToken(user);

    JsonObject obj = new JsonObject();
    obj.addProperty("currentPassword", "1234");
    obj.addProperty("newPassword", "1234");

    mockMvc
        .perform(
            patch("/users/password")
                .header(JwtProperties.HEADER_STRING, prefixToken)
                .content(obj.toString())
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is4xxClientError())
        .andExpect(jsonPath("$.code").value(400))
        .andExpect(jsonPath("$.message").value("현재 비밀번호와 변경할 비밀번호가 동일합니다."))
        .andDo(print());
  }

  @Test
  @Rollback
  @DisplayName("UserController.비밀번호 변경 - 객체가 empty")
  void changeUserNewPassword_json_object_empty() throws Exception {
    User user = User.builder().email("test@test").password("1234").build();
    String prefixToken = JwtProperties.TOKEN_PREFIX + JwtUtils.createAccessToken(user);

    JsonObject obj = new JsonObject();

    mockMvc
        .perform(
            patch("/users/password")
                .header(JwtProperties.HEADER_STRING, prefixToken)
                .content(obj.toString())
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is4xxClientError())
        .andExpect(jsonPath("$.code").value(400))
        .andExpect(jsonPath("$.message").value("현재 비밀번호가 존재하지 않습니다."))
        .andDo(print());
  }

  @Test
  @Rollback
  @DisplayName("UserController.비밀번호 변경 - 객체가 null")
  void changeUserNewPassword_json_parameter_null() throws Exception {
    User user = User.builder().email("test@test").password("1234").build();
    String prefixToken = JwtProperties.TOKEN_PREFIX + JwtUtils.createAccessToken(user);

    mockMvc
        .perform(
            patch("/users/password")
                .header(JwtProperties.HEADER_STRING, prefixToken)
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is4xxClientError())
        .andExpect(jsonPath("$.code").value(400))
        .andExpect(jsonPath("$.message").value("비밀번호가 존재하지 않습니다."))
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
            delete("/users")
                .header(JwtProperties.HEADER_STRING, prefixToken)
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is2xxSuccessful())
        .andExpect(jsonPath("$.code").value(200))
        .andExpect(jsonPath("$.message").value("회원탈퇴가 정상적으로 이뤄졌습니다."))
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
            patch("/users/admin")
                .header(JwtProperties.HEADER_STRING, prefixToken)
                .content(Constant.ADMIN_CODE)
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is2xxSuccessful())
        .andExpect(jsonPath("$.code").value(200))
        .andExpect(jsonPath("$.message").value("관리자 권한을 부여 받았습니다."))
        .andDo(print());
  }

  @Test
  @Rollback
  @DisplayName("UserController.관리자 - 관리자 코드 공백")
  void changeUserRoleEmptyAdminCode_empty_fail() throws Exception {
    User user = User.builder().email("test@test").password("1234").build();
    String prefixToken = JwtProperties.TOKEN_PREFIX + JwtUtils.createAccessToken(user);

    mockMvc
        .perform(
            patch("/users/admin")
                .header(JwtProperties.HEADER_STRING, prefixToken)
                .content("")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is4xxClientError())
        .andExpect(jsonPath("$.code").value(400))
        .andExpect(jsonPath("$.message").value("코드가 존재하지 않습니다."))
        .andDo(print());
  }

  @Test
  @Rollback
  @DisplayName("UserController.관리자 - 관리자 코드 일치하지 않음")
  void changeUserRoleEmptyAdminCode_no_same_fail() throws Exception {
    User user = User.builder().email("test@test").password("1234").build();
    String prefixToken = JwtProperties.TOKEN_PREFIX + JwtUtils.createAccessToken(user);

    mockMvc
        .perform(
            patch("/users/admin")
                .header(JwtProperties.HEADER_STRING, prefixToken)
                .content("asdadsadas")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is4xxClientError())
        .andExpect(jsonPath("$.code").value(400))
        .andExpect(jsonPath("$.message").value("관리자 코드가 일치하지 않습니다."))
        .andDo(print());
  }

  @Test
  @Rollback
  @DisplayName("UserController.관리자 - 관리자 코드 없음")
  void changeUserRoleEmptyAdminCode_null_fail() throws Exception {
    User user = User.builder().email("test@test").password("1234").build();
    String prefixToken = JwtProperties.TOKEN_PREFIX + JwtUtils.createAccessToken(user);

    mockMvc
        .perform(
            patch("/users/admin")
                .header(JwtProperties.HEADER_STRING, prefixToken)
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is4xxClientError())
        .andExpect(jsonPath("$.code").value(400))
        .andExpect(jsonPath("$.message").value("코드가 존재하지 않습니다."))
        .andDo(print());
  }
}
