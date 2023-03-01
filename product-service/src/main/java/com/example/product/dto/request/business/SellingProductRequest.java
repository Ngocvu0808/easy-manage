package com.example.product.dto.request.business;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class SellingProductRequest {

  private List<RequestData> products;
  private String source;
  private String customerPhone;
  private Integer userId;
}
