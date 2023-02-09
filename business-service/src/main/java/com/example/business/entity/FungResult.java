package com.example.business.entity;

public enum FungResult {
  SUCCESSED(0),
  FAILED(1);

  private final Integer value;

  private FungResult(Integer value) {
    this.value = value;
  }

  public Integer getValue() {
    return this.value;
  }
}
