package com.example.product.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetProductResponse {
  private int id;
  private String code;
  private String name;
  private String type;
  private long buyPrice;
  private String link;
  private long createDate;
  private long sellPrice;
  private long discountPrice;
  private String saleOff;
  private long available;
  private String status;
}
