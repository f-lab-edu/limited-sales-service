package com.limited.sales.product.vo;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Product implements Serializable{

  private Integer id;
  private String name;
  private Integer price;
  private Integer quantity;
  private String details;
  private Status status;
  private Integer fileGroupId;
  private LocalDateTime createDateTime;
  private LocalDateTime salesTime;
  private LocalDateTime endTime;

  public Product (
      String name,
      int price,
      int quantity,
      String details,
      Status status,
      int fileGroupId,
      LocalDateTime createDateTime,
      LocalDateTime salesTime,
      LocalDateTime endTime) {
    this.name = name;
    this.price = price;
    this.quantity = quantity;
    this.details = details;
    this.status = status;
    this.fileGroupId = fileGroupId;
    this.createDateTime = createDateTime;
    this.salesTime = salesTime;
    this.endTime = endTime;
  }

  /** 상품 상태 정보 */
  public enum Status {
    Y("Y"),
    N("N");

    private String value;

    Status(String value) {
      this.value = value;
    }

    public String getValue() {
      return value;
    }
  }
}
