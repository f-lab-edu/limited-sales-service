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
import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/products", produces = MediaType.APPLICATION_JSON_VALUE)
public class ProductController {

  private final ProductService productService;

  /**
   * 상품 조회
   *
   * @param productId
   * @return
   */
  @GetMapping("{id}")
  public HttpResponse<Product> findProduct(
      @PathVariable("id") @NotNull(message = "상품 아이디가 존재하지 않습니다.") final Integer productId) {
    final Product foundProduct = productService.findProduct(productId);

    if (foundProduct == null) {
      return HttpResponse.toResponse(HttpStatus.BAD_REQUEST, "상품이 존재하지 않습니다.", null);
    } else {
      return HttpResponse.toResponse(HttpStatus.OK, "상품 조회 성공", foundProduct);
    }
  }

  /**
   * 상품 전체 조회
   *
   * @return
   */
  @GetMapping
  public HttpResponse<List<Product>> findProductList() {
    final List<Product> foundProductList = productService.finalProductsList();

    if (foundProductList == null) {
      return HttpResponse.toResponse(HttpStatus.BAD_REQUEST, "상품 목록이 존재하지 않습니다.", null);
    } else {
      return HttpResponse.toResponse(HttpStatus.OK, "상품 목록 조회 성공", foundProductList);
    }
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

  /**
   * 수량 변경
   *
   * @param productId
   * @param quantity
   * @return
   */
  @PatchMapping("/{id}")
  public HttpResponse<Void> updateQuantity(
      @PathVariable("id") @NotNull(message = "상품 아이디가 존재하지 않습니다.") final Integer productId,
      @RequestParam @NotNull(message = "상품 수량이 존재하지 않습니다.") final Integer quantity) {

    if (productService.updateQuantity(productId, quantity) > 0) {
      return HttpResponse.toResponse(HttpStatus.OK, "수량이 변경되었습니다.");
    } else {
      return HttpResponse.toResponse(HttpStatus.BAD_REQUEST, "수량 변경을 실패했습니다.");
    }
  }

  /**
   * 상품 삭제
   *
   * @param productId
   * @return
   */
  @DeleteMapping("/{id}")
  public HttpResponse<Void> productDelete(
      @PathVariable("id") @NotNull(message = "상품 아이디가 존재하지 않습니다.") final Integer productId) {

    if (productService.deleteProduct(productId) > 0) {
      return HttpResponse.toResponse(HttpStatus.OK, "상품이 삭제되었습니다.");
    } else {
      return HttpResponse.toResponse(HttpStatus.BAD_REQUEST, "상품 삭제 실패.");
    }
  }
}
