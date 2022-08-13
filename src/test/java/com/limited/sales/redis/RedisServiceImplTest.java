package com.limited.sales.redis;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(value = "classpath:/application.properties")
@Transactional
@Slf4j
class RedisServiceImplTest {

  @Autowired RedisService redisService;

  @BeforeEach
  void setup() throws Exception {
    redisService.setValue("Test", "Test");
  }

  @AfterEach
  void end() {
    redisService.deleteValue("Test");
  }

  @Test
  @Rollback
  @DisplayName("레디스 SET - 레디스 키, 값 삽입 - 성공")
  void setValue() {
    redisService.setValue("Test", "Test");
    assertThat(redisService.getValue("Test")).isEqualTo(redisService.getValue("Test"));
  }

  @Test
  @DisplayName("레디스 SET - 키 Null - 실패")
  void setValue_key_null() {
    assertThatNullPointerException()
        .isThrownBy(
            () -> {
              redisService.setValue(null, "Test");
            });
  }

  @Test
  @DisplayName("레디스 SET - 키 공백 - 실패")
  void setValue_key_empty() {
    assertThatNullPointerException()
        .isThrownBy(
            () -> {
              redisService.setValue("", "Test");
            });
  }

  @Test
  @DisplayName("레디스 SET - 값 Null - 실패")
  void setValue_value_null() {
    assertThatNullPointerException()
        .isThrownBy(
            () -> {
              redisService.setValue("Test", null);
            });
  }

  @Test
  @DisplayName("레디스 SET - 값 공백 - 실패")
  void setValue_value_empty() {
    assertThatNullPointerException()
        .isThrownBy(
            () -> {
              redisService.setValue("Test", "");
            });
  }

  @Test
  @DisplayName("레디스 GET - 키와 값 호출 - 성공")
  void getValue() {
    assertThat(redisService.getValue("Test")).isNotNull().isEqualTo("Test");
  }

  @Test
  @DisplayName("레디스 GET - 키 null - 실패")
  void getValue_key_null() {
    assertThatNullPointerException()
        .isThrownBy(
            () -> {
              redisService.getValue(null);
            });
  }

  @Test
  @DisplayName("레디스 GET - 키 공백 - 실패")
  void getValue_key_empty() {
    assertThatNullPointerException()
        .isThrownBy(
            () -> {
              redisService.getValue("");
            });
  }

  @Test
  @DisplayName("레디스 DELETE - 삭제 - 성공")
  void deleteValue() {
    redisService.deleteValue("Test");
    assertThat(redisService.getValue("Test")).isNull();
  }

  @Test
  @DisplayName("레디스 DELETE - 키 null - 실패")
  void deleteValue_key_null() {
    assertThatNullPointerException()
        .isThrownBy(
            () -> {
              redisService.deleteValue(null);
            });
  }

  @Test
  @DisplayName("레디스 DELETE - 키 공백 - 실패")
  void deleteValue_key_empty() {
    assertThatNullPointerException()
        .isThrownBy(
            () -> {
              redisService.deleteValue("");
            });
  }

  @Test
  @Rollback
  @DisplayName("레디스 GET - 데이터 값 한글 테스트 - 성공")
  void deleteValue_key_kor_encoded() {
    redisService.setValue("한글", "안녕하세요.");
    assertThat(redisService.getValue("한글")).isNotNull().isEqualTo("안녕하세요.");
  }

  @Test
  @Rollback
  @DisplayName("레디스 GET - 데이터 값 한글 테스트 - 성공")
  void deleteValue_value_kor_encoded() {
    redisService.setValue("kor", "안녕하세요.");
    assertThat(redisService.getValue("kor")).isNotNull().isEqualTo("안녕하세요.");
  }
}
