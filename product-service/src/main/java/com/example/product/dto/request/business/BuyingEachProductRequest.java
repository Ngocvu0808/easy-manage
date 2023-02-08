package com.example.product.dto.request.business;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BuyingEachProductRequest {

  private String source;
  private int productId;
  private int amount;
}
