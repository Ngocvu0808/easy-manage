package com.example.product.dto.request.business;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class BuyingProductRequest {

  private Map<Integer, Integer> products;
  private String source;
  private Integer userId;
}
