package com.limited.sales.product;

import com.limited.sales.product.vo.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class productServiceImpl implements ProductService {

  private ProductMapper adminMapper;

  /**
   * 상품등록
   *
   * @param product
   * @return
   */
  @Override
  public void saveProduct(Product product) {
    adminMapper.saveProduct(product);
  }
}
