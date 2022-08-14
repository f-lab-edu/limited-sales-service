package com.limited.sales.utils;

import com.limited.sales.product.vo.Product;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(value = "classpath:/application.properties")
@Transactional
@Slf4j
class ProducerTest {

  @Autowired MockMvc mockMvc;
  @Autowired
  Producer consumer;
  @Autowired private WebApplicationContext context;

  @BeforeEach
  public void setup() {
    mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
  }

  @Test
  @DisplayName("mq consumer test")
  void send() {
    Product product =
        new Product(
            1,
            "맥북프로",
            1000000,
            1000,
            "16인치",
            Product.Status.Y,
            1,
            LocalDateTime.of(2022, 07, 22, 9, 00, 0),
            LocalDateTime.of(2022, 07, 22, 10, 00, 0),
            LocalDateTime.of(2022, 07, 22, 12, 00, 0));

    consumer.send(product);
  }
}

