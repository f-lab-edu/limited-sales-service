package com.limited.sales.product;

import com.limited.sales.product.vo.Product;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProductMapper {

  /**
   * 상품 등록
   *
   * @param product
   * @return
   */
  int saveProduct(final Product product);

  /**
   * 상품 정보 가져오기
   *
   * @param productId
   * @return
   */
  Product findByProductId(final Integer productId);
}
