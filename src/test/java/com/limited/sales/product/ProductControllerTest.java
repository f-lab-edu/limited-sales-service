package com.limited.sales.product;

import com.limited.sales.config.Constant;
import com.limited.sales.product.vo.Product;
import com.limited.sales.redis.RedisService;
import com.limited.sales.user.vo.User;
import com.limited.sales.utils.GsonUtils;
import com.limited.sales.utils.JwtProperties;
import com.limited.sales.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(value = "classpath:/application.properties")
@Transactional
@Slf4j
class ProductControllerTest {

  @Autowired MockMvc mockMvc;
  @Autowired ProductMapper productMapper;
  @Autowired RedisService redisService;
  @Autowired private WebApplicationContext context;
  private String jwtAccessToken;

  @BeforeEach
  public void setup() {
    mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
    User user =
        User.builder().email("test@test").password("1234").role(Constant.ROLE_ADMIN).build();
    String accessTokenMethod = JwtUtils.createAccessToken(user);
    jwtAccessToken = JwtProperties.TOKEN_PREFIX + accessTokenMethod;
  }

  @Test
  @DisplayName("상품등록성공")
  void SucceedOrder() {

    // 참고로 myBatis는 기본적으로 setter 매커니즘을 이용합니다.
    // 따라서 기존에 작성한 @Builder를 사용할 경우, insert 후 자동으로 해당 pk key를 가져오는 기능을
    // 사용하기 어렵습니다. 따라서 다시 Setter를 사용하도록 변경해주었습니다.
    Product product =
        new Product(
            "맥북프로",
            1000000,
            1000,
            "16인치",
            Product.Status.Y,
            1,
            LocalDateTime.of(2022, 07, 22, 9, 00, 0),
            LocalDateTime.of(2022, 07, 22, 10, 00, 0),
            LocalDateTime.of(2022, 07, 22, 12, 00, 0));

    productMapper.saveProduct(product);
    int productId = product.getId();
    Product resultProduct = productMapper.findByProductId(productId);

    assertThat(product).usingRecursiveComparison().isEqualTo(resultProduct);
  }

  @Test
  @DisplayName("Redis 수량 저장")
  void SaveCount() throws Exception {

    Product product =
        new Product(
            "맥북프로",
            1000000,
            1000,
            "16인치",
            Product.Status.Y,
            2,
            LocalDateTime.of(2022, 07, 22, 9, 00, 0),
            LocalDateTime.of(2022, 07, 22, 10, 00, 0),
            LocalDateTime.of(2022, 07, 22, 12, 00, 0));

    log.debug("ddddd={}", product);

    GsonUtils gson = new GsonUtils();
    String toJson = gson.toJson(product);

    log.debug("toJson={}", toJson);

    mockMvc
        .perform(
            post("/product")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .characterEncoding("utf-8")
                .content(toJson))
        .andExpect(status().isCreated())
        .andDo(print());

    // rollback
    redisService.deleteValue(ProductProperties.PRODUCT_PREFIX + product.getId());
  }
}
