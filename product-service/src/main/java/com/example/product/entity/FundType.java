package com.example.product.entity;

public enum FundType {
  BUYING(0),
  SELLING(1);

  private final Integer value;

  private FundType(Integer value) {
    this.value = value;
  }

  public Integer getValue() {
    return this.value;
  }
}
