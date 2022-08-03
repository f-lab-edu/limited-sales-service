package com.limited.sales.product;

import com.limited.sales.exception.sub.BadRequestException;
import com.limited.sales.product.vo.Product;
import com.limited.sales.redis.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(
    value = "/product",
    produces = MediaType.APPLICATION_JSON_VALUE,
    consumes = MediaType.APPLICATION_JSON_VALUE)
public class ProductController {

  ProductService adminService;

  RedisService redisService;

  @PostMapping
  @Secured("ROLE_ADMIN")
  public ResponseEntity<String> order(@RequestBody Product product) {

    // 1. 상품을 등록한다.
    adminService.saveProduct(product);

    if (product.getProductId() != null) {
      // 2. Redis에 상품 수량을 등록한다.
      redisService.setValue(
          ProductProperties.PRODUCT_PREFIX + product.getProductId(),
          String.valueOf(product.getQuantity()));

      log.debug(
          "value ============= {}",
          redisService.getValue(ProductProperties.PRODUCT_PREFIX + product.getProductId()));
    } else {
      new BadRequestException("상품 정보가 없습니다.");
    }

    return new ResponseEntity<>(HttpStatus.CREATED);
  }
}
