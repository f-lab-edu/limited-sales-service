package com.limited.sales.product;

import com.limited.sales.product.vo.Product;
import com.limited.sales.redis.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
  private final ProductMapper productMapper;
  private final RedisService<Object> redisService;

  @Override
  public int save(final Product product) {
    final int productId = productMapper.saveProduct(product);
    redisService.setValue(ProductProperties.PRODUCT_PREFIX + productId, product.getQuantity());

    return productId;
  }

  /**
   * @param productId
   * @param quantity
   * @return
   */
  @Override
  public int updateQuantity(Integer productId, Integer quantity) {
    return 0;
  }

  /**
   * @param productId
   * @param product
   * @return
   */
  @Override
  public int updateProductInformation(Integer productId, Product product) {
    return 0;
  }

  /**
   * @param productId
   * @return
   */
  @Override
  public int deleteProduct(Integer productId) {
    return 0;
  }
}
