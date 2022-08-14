package com.limited.sales.product;

import com.limited.sales.exception.sub.BadRequestException;
import com.limited.sales.product.vo.Product;
import com.limited.sales.redis.RedisService;
import com.limited.sales.utils.Producer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class productServiceImpl implements ProductService {

  private final ProductMapper productMapper;
  private final RedisService redisService;

  private final Producer consumer;

  /**
   * 상품등록
   * @param product
   * @return
   */
  @Override
  public void save(final Product product) {

    // 1. 관리자가 상품을 등록한다.
    productMapper.saveProduct(product);

    Optional.ofNullable(product)
        .map(Product::getId)
        .orElseThrow(
            () -> {
              throw new BadRequestException("상품 정보가 없습니다.");
            });

    // 2. Redis에 상품 수량을 등록한다.
    redisService.setValue(
        ProductProperties.PRODUCT_PREFIX + product.getId(), String.valueOf(product.getQuantity()));

    log.debug(
        "value ============= {}",
        redisService.getValue(ProductProperties.PRODUCT_PREFIX + product.getId()));
  }

  /**
   * 상품주문
   * @param product
   */
  @Override
  public void order(final Product product) {

    Optional.ofNullable(product)
        .map(Product::getId)
        .orElseThrow(
            () -> {
              throw new BadRequestException("상품 정보가 없습니다.");
            });

    Optional.ofNullable(redisService.getValue(ProductProperties.PRODUCT_PREFIX + product.getId()))
        .orElseThrow(
            () -> {
              throw new BadRequestException("상품 수량 정보가 없습니다.");
            });

    String productId = ProductProperties.PRODUCT_PREFIX + product.getId();

    // 1. Redis에 저장된 상품 id의 수량값을 가져와서 체크한다.
    int productCnt = Integer.valueOf(redisService.getValue(productId));
    log.debug("before redis value ============= {}", productCnt);
    // 수량이 0일 경우 저장을 하지 못한다.
    if (productCnt == 0) {
      throw new BadRequestException("품절입니다.");
    } else {
      // 수량이 0이 아닐 경우, redis에 저장된 숫자를 -1 처리하고, mq 서버로 데이터를 쏴준다.
      redisService.setValue(productId, String.valueOf(productCnt - 1));
      consumer.send(product);
      log.debug("change redis value ============= {}", redisService.getValue(productId));
    }
  }
}
