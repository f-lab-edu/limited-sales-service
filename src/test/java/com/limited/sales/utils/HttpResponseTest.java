package com.limited.sales.utils;

import com.limited.sales.user.UserService;
import com.limited.sales.user.vo.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("classpath:application.properties")
@Transactional
@Validated
class HttpResponseTest {
  @Autowired MockMvc mockMvc;
  @Autowired UserService userService;

  @Test
  @DisplayName("HttpResponse3Ref - 코드, 메세지, null 정상적으로 표출")
  void toResponse_data_null_success() {
    HttpResponse<Void> httpResponse = HttpResponse.toResponse(HttpStatus.OK.value(), "성공", null);
    assertThat(httpResponse.getCode()).isNotNull().isEqualTo(200);
    assertThat(httpResponse.getMessage()).isNotNull().isEqualTo("성공");
    assertThat(httpResponse.getData()).isNull();
  }

  @Test
  @DisplayName("HttpResponse3Ref - 코드, 메세지, 데이터 정상적으로 표출")
  void toResponse_data_success() {
    User byUser = userService.findByEmail("test@test");

    HttpResponse<User> httpResponse = HttpResponse.toResponse(HttpStatus.OK.value(), "성공", byUser);
    assertThat(httpResponse.getCode()).isNotNull().isEqualTo(200);
    assertThat(httpResponse.getMessage()).isNotNull().isEqualTo("성공");
    assertThat(httpResponse.getData().getEmail()).isNotNull().isEqualTo("test@test");
  }

  @Test
  @DisplayName("HttpResponse3Ref - 코드, 메시지, null 성공인데 데이터가 존재하지 않을때")
  void toResponse_no_data() {
    User byUser = userService.findByEmail("test@test");

    HttpResponse<User> httpResponse = HttpResponse.toResponse(200, "성공", null);
    assertThat(httpResponse.getCode()).isNotNull().isEqualTo(200);
    assertThat(httpResponse.getMessage()).isNotNull().isEqualTo("성공");
    assertThat(httpResponse.getData()).isNull();
  }

  @Test
  @DisplayName("HttpResponse3Ref - 코드, 메시지 없음, 데이터")
  void toResponse_no_message() {
    User byUser = userService.findByEmail("test@test");

    HttpResponse<User> httpResponse = HttpResponse.toResponse(200, null, byUser);
    assertThat(httpResponse.getCode()).isNotNull().isEqualTo(200);
    assertThat(httpResponse.getMessage()).isNull();
    assertThat(httpResponse.getData()).isNotNull().isEqualTo(byUser);
  }

  @Test
  @DisplayName("HttpResponse3Ref - 코드가 정상적이지 않음, 메시지, 데이터")
  void toResponse_no_code() {
    User byUser = userService.findByEmail("test@test");

    HttpResponse<User> httpResponse = HttpResponse.toResponse(0, "성공", byUser);
    assertThat(httpResponse.getCode()).isNotNull().isEqualTo(0);
    assertThat(httpResponse.getMessage()).isNotNull().isEqualTo("성공");
    assertThat(httpResponse.getData()).isNotNull().isEqualTo(byUser);
  }

  @Test
  @DisplayName("HttpResponse2Ref - 코드, 메세지 성공")
  void toResponse_data_no_response() {
    User byUser = userService.findByEmail("test@test");

    HttpResponse<Void> httpResponse = HttpResponse.toResponse(200, "성공");
    assertThat(httpResponse.getCode()).isNotNull().isEqualTo(200);
    assertThat(httpResponse.getMessage()).isNotNull().isEqualTo("성공");
    assertThat(httpResponse.getData()).isNull();
  }

  @Test
  @DisplayName("HttpResponse2Ref - ")
  void toResponse() {}
}
