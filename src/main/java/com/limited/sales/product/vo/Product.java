package com.limited.sales.product.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Product implements Serializable {
  private Integer productId;
  private String productName;
  private Integer price;
  private Integer quantity;
  private String details;
  private UseYn useYn;
  private Integer fileGroupId;
  private LocalDateTime createDateTime;
  private LocalDateTime salesTime;
  private LocalDateTime endTime;

  /**
   * 상품 상태 정보
   */
  public enum UseYn {

    Y("Y"),
    N("N");

    private String value;

    UseYn(String value) {
      this.value = value;
    }

    public String getValue() {
      return value;
    }
  }

  public Product(String productName, int price, int quantity, String details, UseYn useYn, int fileGroupId, LocalDateTime createDateTime, LocalDateTime salesTime, LocalDateTime endTime) {
    this.productName = productName;
    this.price = price;
    this.quantity = quantity;
    this.details = details;
    this.useYn = useYn;
    this.fileGroupId = fileGroupId;
    this.createDateTime = createDateTime;
    this.salesTime = salesTime;
    this.endTime = endTime;
  }
}
