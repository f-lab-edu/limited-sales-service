package com.limited.sales.product;

import com.limited.sales.product.vo.Product;
import com.limited.sales.redis.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
  private final ProductMapper productMapper;
  private final RedisService<Object> redisService;

  /**
   * 상품 등록
   *
   * @param product 상품 정보
   * @return
   */
  @Override
  public int save(final Product product) {
    final int productId = productMapper.saveProduct(product);
    redisService.setValue(
        ProductProperties.PRODUCT_QUANTITY_PREFIX + productId, product.getQuantity());
    redisService.setValue(ProductProperties.PRODUCT_PREFIX + productId, product);

    return productId;
  }

  /**
   * 상품 정보 수정
   *
   * @param productId
   * @param product
   * @return
   */
  @Override
  public int updateProductInformation(final Integer productId, final Product product) {
    final Product updateProduct =
        Product.builder()
            .name(product.getName())
            .details(product.getDetails())
            .price(product.getPrice())
            .endTime(product.getEndTime())
            .fileGroupId(product.getFileGroupId())
            .salesStatus(product.getSalesStatus())
            .salesTime(product.getSalesTime())
            .build();
    final int result = productMapper.updateProduct(productId, updateProduct);
    if (result > 0) {
      redisService.deleteValue(ProductProperties.PRODUCT_PREFIX + productId);
    }

    return result;
  }

  /**
   * 상품 수량 수정
   *
   * @param productId
   * @param quantity
   * @return
   */
  @Override
  public int updateQuantity(Integer productId, Integer quantity) {
    final int result = productMapper.updateQuantity(productId, quantity);
    if (result > 0) {
      redisService.deleteValue(ProductProperties.PRODUCT_QUANTITY_PREFIX + productId);
    }
    return result;
  }

  /**
   * 상품 삭제
   *
   * @param productId
   * @return
   */
  @Override
  public int deleteProduct(final Integer productId) {
    final int result = productMapper.deleteProduct(productId);
    if (result > 0) {
      redisService.deleteValue(ProductProperties.PRODUCT_QUANTITY_PREFIX + productId);
      redisService.deleteValue(ProductProperties.PRODUCT_PREFIX + productId);
    }
    return result;
  }

  /**
   * 상품 1건 조회
   *
   * @param productId
   * @return
   */
  @Override
  public Product findProduct(Integer productId) {
    final Product findProduct = productMapper.findByProductId(productId);
    if (findProduct == null) {
      redisService.setValue(ProductProperties.PRODUCT_PREFIX + productId, findProduct);
    }
    return findProduct;
  }

  /**
   * 상품 전체 조회
   *
   * @return
   */
  @Override
  public List<Product> finalProductsList() {
    final List<Product> finalProductsList = productMapper.findProductList();
    if (finalProductsList == null) {
      redisService.setValue(ProductProperties.PRODUCT_LIST, finalProductsList);
    }
    return finalProductsList;
  }
}
