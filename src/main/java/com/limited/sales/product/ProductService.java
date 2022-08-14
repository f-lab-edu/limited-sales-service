package com.limited.sales.product;

import com.limited.sales.product.vo.Product;

public interface ProductService {
  void save(final Product product);

  void order(final Product product);
}
