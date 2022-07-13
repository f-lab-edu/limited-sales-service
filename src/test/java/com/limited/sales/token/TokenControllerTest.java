package com.limited.sales.token;

import com.google.gson.Gson;
import com.limited.sales.user.vo.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(value = "classpath:/application.properties")
@Transactional
@Slf4j
class TokenControllerTest {

  @Autowired MockMvc mockMvc;

  @Test
  void login() throws Exception {
    User user = User.builder().userEmail("ohjeung@naver.com").userPassword("1234").build();
    Gson gson = new Gson();
    String json = gson.toJson(user);

    mockMvc
        .perform(
            post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .characterEncoding("utf-8")
                .content(json))
        .andExpect(status().isOk())
        .andDo(print());
  }

  @Test
  void accessReissue() throws Exception {
    User user = User.builder().userEmail("ohjeung@naver.com").userPassword("1234").build();

    Gson gson = new Gson();
    String json = gson.toJson(user);

    mockMvc
        .perform(
            post("/token")
                .contentType(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .characterEncoding("utf-8")
                .content(json))
        .andExpect(status().isOk())
        .andDo(print());
  }
}
