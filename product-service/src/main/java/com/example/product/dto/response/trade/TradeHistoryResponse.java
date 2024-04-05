package com.example.product.dto.response.trade;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TradeHistoryResponse {

  private int id;

  private String batch;
  private String createDate;
  private int productId;
  private String productName;
  private String productCode;
  private String username;
  private long eachValue;
  private long billValue;
  private String cusName;
  private int amount;
  private String type;
}
