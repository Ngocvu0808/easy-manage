package com.example.product.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateProductRequest {

  private int id;
  private String code;
  private String name;
  private String type;
  private Long buyPrice;
  private Long sellPrice;
  private String source;
  private String status;
}
