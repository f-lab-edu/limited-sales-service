package com.limited.sales.filter;

import com.google.gson.Gson;
import com.limited.sales.config.GsonSingleton;
import com.limited.sales.user.vo.User;
import com.limited.sales.utils.JwtProperties;
import com.limited.sales.utils.JwtValidationUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("classpath:application.properties")
class JwtAuthenticationFilterTest {

  private final Gson gson = GsonSingleton.getGson();
  @Autowired private MockMvc mockMvc;

  /**
   *
   *
   * <h1>JWT - JwtAuthenticationFilter</h1>
   *
   * <h3>attemptAuthentication 메소드와 successfulAuthentication 해당 메소드가 동시에 작동해야 함.</h3>
   */
  @Test
  @DisplayName("JWT인증필터 - 정상적으로 로그인 성공시")
  void attemptAuthentication_And_SuccessfulAuthentication() throws Exception {
    User user = User.builder().email("ohjeung@naver.com").password("1234").build();
    String authorization =
        mockMvc
            .perform(post("/login").content(gson.toJson(user)))
            .andExpect(status().is2xxSuccessful())
            .andExpect(header().exists(JwtProperties.HEADER_STRING))
            .andDo(print())
            .andReturn()
            .getResponse()
            .getHeader(JwtProperties.HEADER_STRING);

    assertThat(authorization).isNotNull().startsWith(JwtProperties.TOKEN_PREFIX);
    assertThat(JwtValidationUtils.isAccessTokenValid(authorization)).isNotNull().isTrue();
  }

  @Test
  @DisplayName("JWT인증필터 - 로그인 정보가 empty")
  void attemptAuthentication_And_SuccessfulAuthentication_Empty() throws Exception {
    User user = User.builder().email("").password("").build();
    String authorization =
        mockMvc
            .perform(post("/login").content(gson.toJson(user)))
            .andExpect(status().is4xxClientError())
            .andExpect(header().doesNotExist(JwtProperties.HEADER_STRING))
            .andDo(print())
            .andReturn()
            .getResponse()
            .getHeader(JwtProperties.HEADER_STRING);

    assertThat(authorization).isNull();
  }

  @Test
  @DisplayName("JWT인증필터 - 로그인 정보가 null")
  void attemptAuthentication_And_SuccessfulAuthentication_Null() throws Exception {
    User user = User.builder().email(null).password(null).build();
    String authorization =
        mockMvc
            .perform(post("/login").content(gson.toJson(user)))
            .andExpect(status().is4xxClientError())
            .andExpect(header().doesNotExist(JwtProperties.HEADER_STRING))
            .andDo(print())
            .andReturn()
            .getResponse()
            .getHeader(JwtProperties.HEADER_STRING);

    assertThat(authorization).isNull();
  }

  @Test
  @DisplayName("JWT인증필터 - User null")
  void attemptAuthentication_And_SuccessfulAuthentication_UserNull() throws Exception {
    User user = null;
    String authorization =
        mockMvc
            .perform(post("/login").content(gson.toJson(user)))
            .andExpect(status().is4xxClientError())
            .andExpect(header().doesNotExist(JwtProperties.HEADER_STRING))
            .andDo(print())
            .andReturn()
            .getResponse()
            .getHeader(JwtProperties.HEADER_STRING);

    assertThat(authorization).isNull();
  }

  @Test
  @DisplayName("JWT인증필터 - User 비밀번호 null")
  void attemptAuthentication_And_SuccessfulAuthentication_PasswordNull() throws Exception {
    User user = User.builder().email("ohjeung@naver.com").password(null).build();
    String authorization =
        mockMvc
            .perform(post("/login").content(gson.toJson(user)))
            .andExpect(status().is4xxClientError())
            .andExpect(header().doesNotExist(JwtProperties.HEADER_STRING))
            .andDo(print())
            .andReturn()
            .getResponse()
            .getHeader(JwtProperties.HEADER_STRING);

    assertThat(authorization).isNull();
  }

  @Test
  @DisplayName("JWT인증필터 - User 이메일 null")
  void attemptAuthentication_And_SuccessfulAuthentication_EmailNull() throws Exception {
    User user = User.builder().email(null).password("1234").build();
    String authorization =
        mockMvc
            .perform(post("/login").content(gson.toJson(user)))
            .andExpect(status().is4xxClientError())
            .andExpect(header().doesNotExist(JwtProperties.HEADER_STRING))
            .andDo(print())
            .andReturn()
            .getResponse()
            .getHeader(JwtProperties.HEADER_STRING);

    assertThat(authorization).isNull();
  }
}
