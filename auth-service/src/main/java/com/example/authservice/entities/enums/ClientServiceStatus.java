package com.example.authservice.entities.enums;

public enum ClientServiceStatus {
  ACTIVE(0), DEACTIVE(1);
  private final Integer value;

  ClientServiceStatus(Integer value) {
    this.value = value;
  }

  public Integer getValue() {
    return value;
  }
}
