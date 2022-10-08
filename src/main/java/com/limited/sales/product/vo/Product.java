package com.limited.sales.product.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@ToString
@NoArgsConstructor
public class Product implements Serializable {

  private Integer id;

  @NotBlank(message = "상품 이름이 존재하지 않습니다.")
  private String name;

  @NotNull(message = "상품 가격이 존재하지 않습니다.")
  private Integer price;

  @NotNull(message = "상품 수량이 존재하지 않습니다.")
  private Integer quantity;

  @NotBlank(message = "상품 설명이 존재하지 않습니다.")
  private String details;

  @NotNull(message = "상품 판매 여부가 존재하지 않습니다.")
  private SalesStatus salesStatus;

  @NotNull(message = "상품 이미지가 존재하지 않습니다.")
  private Integer fileGroupId;

  private LocalDateTime createDateTime;

  @NotNull(message = "상품 판매시작 시간이 존재하지 않습니다.")
  @JsonFormat(
      shape = JsonFormat.Shape.STRING,
      pattern = "yyyy-MM-dd HH:mm:ss",
      timezone = "Asia/Seoul")
  private LocalDateTime salesTime;

  @NotNull(message = "상품 판매종료 시간이 존재하지 않습니다.")
  @JsonFormat(
      shape = JsonFormat.Shape.STRING,
      pattern = "yyyy-MM-dd HH:mm:ss",
      timezone = "Asia/Seoul")
  private LocalDateTime endTime;

  @Builder
  public Product(
      String name,
      Integer price,
      Integer quantity,
      String details,
      SalesStatus salesStatus,
      Integer fileGroupId,
      LocalDateTime createDateTime,
      LocalDateTime salesTime,
      LocalDateTime endTime) {
    this.name = name;
    this.price = price;
    this.quantity = quantity;
    this.details = details;
    this.salesStatus = salesStatus;
    this.fileGroupId = fileGroupId;
    this.createDateTime = createDateTime;
    this.salesTime = salesTime;
    this.endTime = endTime;
  }

  /** 상품 상태 정보 */
  public enum SalesStatus {
    SALE("Y"),
    UNSOLD("N");

    private final String status;

    SalesStatus(final String status) {
      this.status = status;
    }

    public String getStatus() {
      return status;
    }
  }
}
