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
  private long buyPrice;
  private String source;
  private long inDate;
  private String status;
}
