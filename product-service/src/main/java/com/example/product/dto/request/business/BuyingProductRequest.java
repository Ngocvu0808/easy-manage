package com.example.product.dto.request.business;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class BuyingProductRequest {

  private List<RequestData> products;
  private String source;
  private Integer userId;

}
