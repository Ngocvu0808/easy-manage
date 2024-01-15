package com.example.product.dto.request.business;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FundRequestData {

  private String uuid;
  private long amount;
  private String customer;
  private String type;
  private String batch;
  private Integer userId;
  private String paymentType;
}
