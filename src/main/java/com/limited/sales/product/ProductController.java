package com.limited.sales.product;

import com.limited.sales.exception.sub.BadRequestException;
import com.limited.sales.product.vo.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

  private final ProductService productService;

  /**
   * 상품정보 등록하기
   * @param product
   * @return
   */
  @PostMapping
  // @Secured(Constant.ROLE_ADMIN)
  public ResponseEntity<String> saveProduct(@RequestBody final Product product) {
    validateProduct(product);
    productService.save(product);
    return new ResponseEntity<>(HttpStatus.CREATED);
  }

  /**
   * 주문하기
   * @param product
   * @return
   */
  @PostMapping("/order")
  public ResponseEntity<String> orderProduct(@RequestBody final Product product) {
    validateProduct(product);
    productService.order(product);
    return new ResponseEntity<>(HttpStatus.CREATED);
  }

  /**
   * product null check
   *
   * @param product
   */
  private void validateProduct(Product product) {
    Optional.ofNullable(product)
        .orElseThrow(
            () -> {
              throw new BadRequestException("상품정보를 다시 입력해주세요.");
            });
  }
}
