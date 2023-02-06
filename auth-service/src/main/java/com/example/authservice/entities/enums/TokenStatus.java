package com.example.authservice.entities.enums;

public enum TokenStatus {
  ACTIVE(0), DEACTIVE(1), PENDING(3), EXPIRED(4), REJECTED(5);
  private final Integer value;

  TokenStatus(Integer value) {
    this.value = value;
  }

  public Integer getValue() {
    return value;
  }
}
