package com.limited.sales.product;

import com.limited.sales.config.Constant;
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

import java.util.Optional;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(
    value = "/product",
    produces = MediaType.APPLICATION_JSON_VALUE,
    consumes = MediaType.APPLICATION_JSON_VALUE)
public class ProductController {

  private final ProductService adminService;

  @PostMapping
  //@Secured(Constant.ROLE_ADMIN)
  public ResponseEntity<String> order(@RequestBody final Product product) {

    Optional.ofNullable(product)
        .orElseThrow(
            () -> {
              throw new BadRequestException("상품정보를 다시 입력해주세요.");
            });

    adminService.saveProduct(product);
    return new ResponseEntity<>(HttpStatus.CREATED);
  }
}
