package com.example.product.entity;

public enum ProductStatus {
  ACTIVE(0),
  DEACTIVATE(1);

  private final Integer value;

  private ProductStatus(Integer value) {
    this.value = value;
  }

  public Integer getValue() {
    return this.value;
  }
}
