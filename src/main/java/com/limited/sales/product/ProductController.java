package com.limited.sales.product;

import com.limited.sales.product.vo.Product;
import com.limited.sales.utils.HttpResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/products", produces = MediaType.APPLICATION_JSON_VALUE)
public class ProductController {

  private final ProductService productService;

  /**
   * @param product
   * @return
   */
  @GetMapping
  public HttpResponse<Void> findProduct() {
    return null;
  }

  /**
   * 상품 등록
   *
   * @param product 상품 정보
   * @return HttpStatus.CREATED, message = 상품이 등록되었습니다.
   */
  @PostMapping
  public HttpResponse<Void> saveProduct(
      @RequestBody @Valid @NotNull(message = "상품이 존재하지 않습니다.") final Product product) {

    if (productService.save(product) > 0) {
      return HttpResponse.toResponse(HttpStatus.CREATED, "상품이 등록되었습니다.");
    } else {
      return HttpResponse.toResponse(HttpStatus.BAD_REQUEST, "상품등록을 실패했습니다.");
    }
  }

  /**
   * 상품 상세 정보 수정
   *
   * @param productId 수정할 상품 번호
   * @param product 상품 정보
   * @return HttpStatus.OK, "상품이 수정되었습니다.", HttpStatus.BAD_REQUEST, "상품을 수정하는데 실패했습니다."
   */
  @PutMapping("/{id}")
  public HttpResponse<Void> updateProduct(
      @PathVariable("id") @NotNull(message = "상품 아이디가 존재하지 않습니다.") final Integer productId,
      @RequestBody @Valid @NotNull(message = "상품이 존재하지 않습니다.") final Product product) {

    if (productService.updateProductInformation(productId, product) > 0) {
      return HttpResponse.toResponse(HttpStatus.OK, "상품이 수정되었습니다.");
    } else {
      return HttpResponse.toResponse(HttpStatus.BAD_REQUEST, "상품을 수정하는데 실패했습니다.");
    }
  }
}
