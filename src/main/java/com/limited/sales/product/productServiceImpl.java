package com.limited.sales.product;

import com.limited.sales.exception.sub.BadRequestException;
import com.limited.sales.product.vo.Product;
import com.limited.sales.redis.RedisService;
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

  /**
   * 상품등록
   *
   * @param product
   * @return
   */
  @Override
  public void saveProduct(final Product product) {

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
        ProductProperties.PRODUCT_PREFIX + product.getId(),
        String.valueOf(product.getQuantity()));

    log.debug(
        "value ============= {}",
        redisService.getValue(ProductProperties.PRODUCT_PREFIX + product.getId()));
  }
}
