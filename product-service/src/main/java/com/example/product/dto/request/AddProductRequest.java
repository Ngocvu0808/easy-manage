package com.example.product.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddProductRequest {

  private String name;
  private String type;
  private long buyPrice;
  private long sellPrice;
  private String link;
}
