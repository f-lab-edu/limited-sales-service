package com.limited.sales.token;

import com.google.gson.Gson;
import com.limited.sales.config.LazyHolderObject;
import com.limited.sales.user.vo.User;
import com.limited.sales.utils.JwtProperties;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(value = "classpath:/application.properties")
@Transactional
@Slf4j
class TokenControllerTest {

  private final Gson gson = LazyHolderObject.getGson();
  @Autowired private MockMvc mockMvc;
  String prefixAccessToken;

  @BeforeEach
  void setup() throws Exception {
    User user = User.builder().userEmail("test@test").userPassword("1234").build();

    prefixAccessToken =
        mockMvc
            .perform(
                post("/login").content(gson.toJson(user)).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().is2xxSuccessful())
            .andDo(print())
            .andReturn()
            .getResponse()
            .getHeader(JwtProperties.HEADER_STRING);
  }

  /**
   *
   *
   * <h1>엑세스 토큰 재발급 기능</h1>
   */
  @Test
  @Rollback
  @DisplayName("TokenController.엑세스 토큰 재발급 - 토큰 재발급 정상")
  void reissueAccessToken() throws Exception {
    mockMvc
        .perform(
            post("/token")
                .header(JwtProperties.HEADER_STRING, prefixAccessToken)
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is2xxSuccessful())
        .andExpect(jsonPath("$.accessToken", JwtProperties.TOKEN_PREFIX).exists())
        .andDo(print());
  }
}
