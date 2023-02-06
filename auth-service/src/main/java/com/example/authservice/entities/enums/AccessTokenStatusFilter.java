package com.example.authservice.entities.enums;

public enum AccessTokenStatusFilter {
  ACTIVE(0), PENDING(1), EXPIRED(2), REJECTED(3);
  private final Integer value;

  AccessTokenStatusFilter(Integer value) {
    this.value = value;
  }

  public Integer getValue() {
    return value;
  }
}
