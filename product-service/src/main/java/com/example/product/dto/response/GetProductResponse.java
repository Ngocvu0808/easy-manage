package com.example.product.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetProductResponse {

  private String code;
  private String name;
  private String type;
  private long buyPrice;
  private String source;
  private long createDate;
  private long sellPrice;
  private long available;
  private String status;
}
