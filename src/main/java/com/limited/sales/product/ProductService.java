package com.limited.sales.product;

import com.limited.sales.product.vo.Product;

public interface ProductService {
  /**
   * 상품 등록
   *
   * @param product 상품 정보
   */
  int save(final Product product);
  /*
  TODO :: 상품 수량 수정 -> Redis 캐시
  */
  int updateQuantity(final Integer productId, final Integer quantity);
  /*
   TODO :: 상품 이름, 상품 상세, 상품 가격, 상품 이미지, 상품 상태, 상품 시작시간, 상품 종료시간 수정
  */
  int updateProductInformation(final Integer productId, final Product product);

  /*
  TODO :: 상품 삭제
  */
  int deleteProduct(final Integer productId);

  /*
    TODO :: 상품 조회,
   */
}
