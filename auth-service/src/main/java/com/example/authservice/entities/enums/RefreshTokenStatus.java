package com.example.authservice.entities.enums;

public enum RefreshTokenStatus {
  ACTIVE(0), DEACTIVE(1), PENDING(3), EXPIRED(4), REJECTED(5);
  private final Integer value;

  RefreshTokenStatus(Integer value) {
    this.value = value;
  }

  public Integer getValue() {
    return value;
  }
}
