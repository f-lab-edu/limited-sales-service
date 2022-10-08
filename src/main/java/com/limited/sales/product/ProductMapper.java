package com.limited.sales.product;

import com.limited.sales.product.vo.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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

  /**
   * 상품 정보 수정
   * @param id
   * @param product
   * @return
   */
  int updateProduct(@Param("id") final Integer id, @Param("product") final Product product);

  /**
   * 상품 수량 수정
   * @param productId
   * @param quantity
   * @return
   */
  int updateQuantity(@Param("productId") final Integer productId, @Param("quantity") final Integer quantity);
}
