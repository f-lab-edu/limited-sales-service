package com.limited.sales.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.limited.sales.user.vo.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@WebAppConfiguration
@Slf4j
@Transactional //  @Transactional의 default는 rollback true
class UserControllerTest {

  @Autowired private WebApplicationContext context;

  @Autowired ObjectMapper objectMapper;

  @Autowired MockMvc mockMvc;

  @BeforeEach
  void setUp() {
    mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
  }

  @AfterEach
  void tearDown() {}

  @Test
  @Rollback
  @DisplayName("회원가입")
  @WithAnonymousUser
  void join() throws Exception {

    User user =
        User.builder()
            .userEmail("daram@naver.com")
            .userPassword("1234")
            .userCellphone("01012341234")
            .userRole("ROLE_USER")
            .useYn("Y")
            .build();

    String json = objectMapper.writeValueAsString(user);

    mockMvc
        .perform(
            post("/user")
                .header("Authorization", "limited")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .content(json))
        .andExpect(status().isCreated())
        .andDo(print());
  }

  @Test
  @DisplayName("이메일 중복 체크")
  @WithAnonymousUser
  void userEmailChk() throws Exception {
    // 파라미터 셋팅
    User user = User.builder().userEmail("daramii@naver.com").build();

    String json = objectMapper.writeValueAsString(user);

    mockMvc
        .perform(
            get("/user/email")
                .header("Authorization", "limited")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
        .andDo(print())
        .andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName("회원정보 수정")
  void userInfoChange() throws Exception {
    // 파라미터 셋팅
    User user = User.builder().userEmail("daramii@naver.com").userCellphone("1111").build();

    String json = objectMapper.writeValueAsString(user);

    mockMvc
        .perform(
            patch("/user")
                .header("Authorization", getAccessToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
        .andDo(print())
        .andExpect(status().isOk());
  }

  @Test
  void userInfoDelete() throws Exception {
    // 파라미터 셋팅
    User user = User.builder().userEmail("daramii@naver.com").userPassword("1234").build();

    String json = objectMapper.writeValueAsString(user);

    mockMvc
        .perform(
            delete("/user")
                .header("Authorization", getAccessToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
        .andDo(print())
        .andExpect(status().isOk());
  }

  @Test
  void userChangeAdmin() throws Exception {
    // 파라미터 셋팅
    User user = User.builder().userEmail("daramii@naver.com").build();

    String json = objectMapper.writeValueAsString(user);

    mockMvc
        .perform(
            patch("/user/admin")
                .header("Authorization", getAccessToken())
                .header("adminCode", "ADMIdN")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
        .andDo(print())
        .andExpect(status().isBadRequest());
  }

  @Test
  void reIssue() throws Exception {

    mockMvc
        .perform(
            get("/re-issue")
                .header("Authorization", getAccessToken())
                .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk());
  }

  @Test
  void logout() throws Exception {
    mockMvc
        .perform(
            get("/logout")
                .header("Authorization", getAccessToken())
                .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk());
  }

  private String getAccessToken() throws Exception {
    // 파라미터 셋팅
    User user = User.builder().userEmail("daramii@naver.com").userPassword("1234").build();

    String json = objectMapper.writeValueAsString(user);

    String access_token =
        mockMvc
            .perform(
                post("/login")
                    .header("Authorization", "limited")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json))
            .andReturn()
            .getResponse()
            .getHeader("Authorization");

    return access_token;
  }
}
