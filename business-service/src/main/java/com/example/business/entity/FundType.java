package com.example.business.entity;

public enum FundType {
  BUYING(0),
  SELLING(1),
  SALARY(2);

  private final Integer value;

  private FundType(Integer value) {
    this.value = value;
  }

  public Integer getValue() {
    return this.value;
  }
}
